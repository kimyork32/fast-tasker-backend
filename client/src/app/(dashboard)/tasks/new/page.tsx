'use client'; 

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { createTask } from '@/services/task.service';
import { getMyProfile } from '@/services/account.service'; // Necesario para 'posterId'
import { TaskRequest, Account } from '@/lib/types';

export default function NewTaskPage() {
  const [account, setAccount] = useState<Account | null>(null);
  const [isLoadingAccount, setIsLoadingAccount] = useState(true);
  const router = useRouter();

  // Estados del formulario para el DTO
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [budget, setBudget] = useState('0'); // Empezar como string
  const [taskDate, setTaskDate] = useState('');
  const [address, setAddress] = useState(''); // Location (simple)
  
  const [error, setError] = useState<string | null>(null);

  // 1. Obtener el perfil del usuario al cargar la página.
  // Lo necesitamos para obtener el 'posterId' que exige el DTO.
  useEffect(() => {
    getMyProfile()
      .then(setAccount)
      .catch(() => {
        setError('Debes iniciar sesión para crear una tarea');
        router.push('/login');
      })
      .finally(() => setIsLoadingAccount(false));
  }, [router]);


  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    // Validación
    if (!account) {
      setError('Error: no se pudo verificar el usuario.');
      return;
    }

    try {
      // 2. Construir el DTO 'TaskRequest'
      const taskRequest: TaskRequest = {
        title,
        description,
        budget: parseInt(budget, 10) || 0, // Convertir a número
        taskDate: new Date(taskDate).toISOString(), // Convertir a ISO String
        status: 'OPEN', // Por defecto al crear
        posterId: account.id, // ¡ID del usuario logueado!
        location: { // Objeto de ubicación simple
          address: address,
          city: 'Arequipa', // Podrías tener más campos
          country: 'Peru',
        }
      };
      
      // 3. Llamar al servicio
      const newTask = await createTask(taskRequest);
      
      alert(`Tarea "${newTask.title}" creada con éxito.`);
      router.push(`/tasks/${newTask.id}`); // Redirigir a la nueva tarea

    } catch (err) {
      setError((err as Error).message);
    }
  };

  if (isLoadingAccount) {
    return <div>Cargando...</div>;
  }

  return (
    <div>
      <h1>Crear Nueva Tarea</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="title">Título:</label>
          <input
            id="title" type="text" value={title}
            onChange={(e) => setTitle(e.target.value)} required
          />
        </div>
        <div>
          <label htmlFor="description">Descripción:</label>
          <textarea
            id="description" value={description}
            onChange={(e) => setDescription(e.target.value)} required
          />
        </div>
        <div>
          <label htmlFor="budget">Presupuesto (S/.):</label>
          <input
            id="budget" type="number" value={budget}
            onChange={(e) => setBudget(e.target.value)} required
          />
        </div>
        <div>
          <label htmlFor="taskDate">Fecha y Hora:</label>
          <input
            id="taskDate" type="datetime-local" value={taskDate}
            onChange={(e) => setTaskDate(e.target.value)} required
          />
        </div>
        <div>
          <label htmlFor="address">Dirección:</label>
          <input
            id="address" type="text" value={address}
            onChange={(e) => setAddress(e.target.value)} required
          />
        </div>

        <button type="submit">Publicar Tarea</button>
        {error && <p style={{ color: 'red' }}>Error: {error}</p>}
      </form>
    </div>
  );
}