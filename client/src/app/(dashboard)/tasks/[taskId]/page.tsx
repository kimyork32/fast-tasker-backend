// src/app/(dashboard)/tasks/[taskId]/page.tsx
"use client"; // <-- Convertido a Componente de Cliente

import { useState, useEffect } from 'react';
import { Task } from '@/lib/types';
import { getTaskById } from '@/services/task.service';

type TaskDetailPageProps = {
  params: {
    taskId: string; // El nombre [taskId] de la carpeta
  };
};

export default function TaskDetailPage({ params }: TaskDetailPageProps) {
  const { taskId } = params;
  const [task, setTask] = useState<Task | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!taskId) return; // No hacer nada si no hay ID

    const loadTask = async () => {
      try {
        setIsLoading(true);
        const data = await getTaskById(taskId); // Llama al servicio
        setTask(data);
      } catch (err) {
        setError((err as Error).message);
      } finally {
        setIsLoading(false);
      }
    };

    loadTask();
  }, [taskId]); // Se vuelve a ejecutar si el taskId cambia

  if (isLoading) return <div>Cargando tarea...</div>;
  if (error) return <div style={{ color: 'red' }}>Error: {error}</div>;
  if (!task) return <div>Tarea no encontrada</div>;

  // 3. Renderizar los detalles
  return (
    <div>
      <h1>{task.title}</h1>
      <p>{task.description}</p>
      <hr />
      <p>
        <strong>Estado:</strong> {task.status}
      </p>
      <p>
        <strong>Publicada por:</strong> {task.posterId}
      </p>
      <button>Aplicar para esta tarea</button>
    </div>
  );
}