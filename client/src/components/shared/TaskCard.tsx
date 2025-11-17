// src/components/shared/TaskCard.tsx
import { Task } from '@/lib/types';
import Link from 'next/link';

type TaskCardProps = {
  task: Task;
};

export function TaskCard({ task }: TaskCardProps) {
  return (
    <div style={{ border: '1px solid gray', padding: '1rem', margin: '0.5rem' }}>
      <h3>{task.title}</h3>
      <p>{task.description}</p>
      <span>Estado: {task.status}</span>
      <br />
      <Link href={`/tasks/${task.id}`}>Ver detalles</Link>
    </div>
  );
}