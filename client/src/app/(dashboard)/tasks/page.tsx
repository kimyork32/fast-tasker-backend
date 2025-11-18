// src/app/(dashboard)/tasks/page.tsx
"use client"; // <-- Convertido a Componente de Cliente

import { useState, useEffect } from 'react';
import { TaskResponse } from '@/lib/types'; // Importa tu tipo
import { getPublicTasks } from '@/services/task.service'; // ¡Importamos el servicio REAL!
import { TaskCard } from '@/components/shared/TaskCard'; // Asumiendo que tienes este componente

export default function PublicTasksPage() {
  const [tasks, setTasks] = useState<TaskResponse[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    // Función async para cargar los datos
    const loadTasks = async () => {
      try {
        setIsLoading(true);
        const data = await getPublicTasks(); // Llama al servicio (que usa fetch)
        setTasks(data);
      } catch (err) {
        setError((err as Error).message);
      } finally {
        setIsLoading(false);
      }
    };

    loadTasks();
  }, []); // El array vacío [] asegura que se ejecute solo 1 vez

  // 6. Renderizado condicional
  if (isLoading) {
    return <div>Cargando tareas...</div>;
  }

  if (error) {
    return <div style={{ color: 'red' }}>Error: {error}</div>;
  }

  return (
    <div>
      <h1>Listado de Tareas</h1>
      <div>
        {tasks.length > 0 ? (
          tasks.map((task) => (
            // Asumo que tienes un componente TaskCard
            // <TaskCard key={task.id} task={task} />
            <div key={task.id} style={{border: '1px solid gray', margin: '10px', padding: '10px'}}>
              <h3>{task.title}</h3>
              <p>{task.status}</p>
            </div>
          ))
        ) : (
          <p>No hay tareas disponibles.</p>
        )}
      </div>
    </div>
  );
}