// src/app/(dashboard)/tasks/[taskId]/page.tsx
"use client";

import { useState, useEffect, use } from 'react'; // <--- 1. IMPORTA 'use'
import { getTaskById } from '@/services/task.service';
import { TaskResponse } from '@/lib/types';

type TaskDetailPageProps = {
  // 2. ACTUALIZA EL TIPO: params es ahora una Promesa
  params: Promise<{
    taskId: string;
  }>;
};

export default function TaskDetailPage({ params }: TaskDetailPageProps) {
  // 3. DESENVUELVE LOS PARAMS USANDO EL HOOK use()
  const { taskId } = use(params);

  // --- A PARTIR DE AQUÍ, LA LÓGICA ES EXACTAMENTE IGUAL A TU OTRO ARCHIVO ---
  
  const [task, setTask] = useState<TaskResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadTaskDetail = async () => {
      try {
        setIsLoading(true);
        // Ahora taskId ya es un string seguro
        const data = await getTaskById(taskId);
        setTask(data);
      } catch (err) {
        console.error(err);
        setError("No se pudo cargar la tarea o no existe.");
      } finally {
        setIsLoading(false);
      }
    };

    loadTaskDetail();
  }, [taskId]); // Dependencia del useEffect

  // Renderizado condicional
  if (isLoading) {
    return <div>Cargando detalles...</div>;
  }

  if (error) {
    return <div style={{ color: 'red' }}>Error: {error}</div>;
  }

  if (!task) {
    return <div>No se encontró la información de la tarea.</div>;
  }

  return (
    <div>
      <h1>{task.title}</h1>
      <div style={{ margin: '20px 0', padding: '15px', border: '1px solid #eee' }}>
        <p><strong>Estado:</strong> {task.status}</p>
        <p><strong>Presupuesto:</strong> S/. {task.budget}</p>
        <p><strong>Fecha:</strong> {task.taskDate}</p>
        
        {task.location && (
            <p><strong>Ubicación:</strong> {task.location.address}, {task.location.city}</p>
        )}
      </div>
      <hr />
      <h3>Descripción</h3>
      <p>{task.description}</p>
    </div>
  );
}