'use client'; 

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { createTask } from '@/services/task.service';
import { getMyProfile } from '@/services/account.service';
import { TaskRequest, Account } from '@/lib/types';

export default function NewTaskPage() {
  const [account, setAccount] = useState<Account | null>(null);
  const [isLoading, setIsLoading] = useState(true); // Estado de carga unificado
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const router = useRouter();

  // Estados del formulario
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [budget, setBudget] = useState('');
  const [taskDate, setTaskDate] = useState('');
  const [address, setAddress] = useState('');

  // Obtener el perfil del usuario para obtener el 'posterId'
  useEffect(() => {
    getMyProfile()
      .then(setAccount)
      .catch(() => {
        // Si falla, redirigir a login. El middleware también lo haría, pero esto es más rápido.
        router.replace('/login');
      })
      .finally(() => setIsLoading(false));
  }, [router]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSuccessMessage(null);

    if (!account) {
      setError('No se pudo verificar la identidad del usuario. Por favor, inicie sesión de nuevo.');
      return;
    }

    setIsLoading(true); // Inicia el estado de carga

    try {
      const taskRequest: TaskRequest = {
        title,
        description,
        budget: parseFloat(budget) || 0,
        taskDate: taskDate,
        location: {
          address: address,
          city: 'Arequipa',
          country: 'Peru',
        }
      };
      
      const newTask = await createTask(taskRequest);
      
      setSuccessMessage(`¡Tarea "${newTask.title}" creada! Redirigiendo...`);

      // Redirigir a la página de la nueva tarea después de un breve momento
      setTimeout(() => {
        router.push(`/tasks/${newTask.id}`);
      }, 2000);

    } catch (err) {
      setError((err as Error).message);
      setIsLoading(false); // Detiene la carga si hay un error
    }
    // No detenemos la carga en caso de éxito, porque la página va a redirigir
  };

  // Muestra un estado de carga general mientras se obtiene el perfil
  if (isLoading && !account) {
    return <div>Cargando tu información...</div>;
  }

  return (
    <div>
      <h1>Crear Nueva Tarea</h1>
      <form onSubmit={handleSubmit}>
        {/* ... campos del formulario ... */}
        <div>
          <label htmlFor="title">Título:</label>
          <input id="title" type="text" value={title} onChange={(e) => setTitle(e.target.value)} required />
        </div>
        <div>
          <label htmlFor="description">Descripción:</label>
          <textarea id="description" value={description} onChange={(e) => setDescription(e.target.value)} required />
        </div>
        <div>
          <label htmlFor="budget">Presupuesto (S/.):</label>
          <input id="budget" type="number" value={budget} onChange={(e) => setBudget(e.target.value)} required min="0" />
        </div>
        <div>
          <label htmlFor="taskDate">Fecha y Hora:</label>
          <input id="taskDate" type="date" value={taskDate} onChange={(e) => setTaskDate(e.target.value)} required />
        </div>
        <div>
          <label htmlFor="address">Dirección:</label>
          <input id="address" type="text" value={address} onChange={(e) => setAddress(e.target.value)} required />
        </div>

        {/* Mensajes de estado */}
        {error && <p style={{ color: 'red' }}>Error: {error}</p>}
        {successMessage && <p style={{ color: 'green' }}>{successMessage}</p>}

        {/* El botón se deshabilita durante la carga para evitar envíos múltiples */}
        <button type="submit" disabled={isLoading}>
          {isLoading ? 'Publicando...' : 'Publicar Tarea'}
        </button>
      </form>
    </div>
  );
}
