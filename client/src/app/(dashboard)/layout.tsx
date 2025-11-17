// src/app/(dashboard)/layout.tsx
// ¡Este archivo se simplifica MUCHO!
// Ya no es 'async', no usa 'getServerSession', no usa 'redirect'.
// El middleware se encarga de la protección.

import React from 'react';
import { Sidebar } from '@/components/shared/Sidebar';
import { Navbar } from '@/components/shared/Navbar'; // Importamos el Navbar actualizado

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {

  // No hay lógica de seguridad aquí.
  // Si el código llega a este layout,
  // es porque el middleware ya dio permiso.

  return (
    <div style={{ display: 'flex', height: '100vh' }}>
      
      <Sidebar />

      <div style={{ display: 'flex', flexDirection: 'column', flex: 1 }}>
        
        {/* El Navbar ahora es independiente */}
        <Navbar />

        <main style={{ padding: '1rem', flex: 1, overflowY: 'auto' }}>
          {children} {/* Aquí se renderiza la página (ej: tu profile/page.tsx) */}
        </main>
      </div>
    </div>
  );
}