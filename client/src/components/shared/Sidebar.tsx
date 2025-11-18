// Este es un Componente de Servidor (por defecto)
import Link from 'next/link';

export function Sidebar() {
  return (
    <aside style={{ borderRight: '1px solid black', padding: '1rem' }}>
      <h2>Fast-Tasker</h2>
      <nav>
        <ul>
          <li>
            <Link href="/dashboard">Dashboard</Link>
          </li>
          <li>
            <Link href="/tasks">Navegar</Link>
          </li>
          <li>
            <Link href="/tasks/new">Crear Tarea</Link>
          </li>
          <li>
            <Link href="/tasks/my">Ver mis tareas</Link>
          </li>
          <li>
            <Link href="/profile">Mi Perfil</Link>
          </li>
        </ul>
      </nav>
    </aside>
  );
}