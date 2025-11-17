// src/components/shared/Navbar.tsx
// Modificado para NO recibir el prop 'user'.
// Ahora es un componente "tonto" o podría
// convertirse en un 'use client' para obtener sus propios datos.

import { LogoutButton } from './LogoutButton'; // Importamos el componente de cliente

export function Navbar() {
  return (
    <header
      style={{
        borderBottom: '1px solid black',
        padding: '1rem',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
      }}
    >
      <div>Buscador...</div>
      <div>
        {/* Aquí podrías tener un componente <UserProfileInfo /> 
          que sea 'use client' y haga un fetch al perfil 
          para mostrar "Hola, {user.name}" 
        */}
        <LogoutButton />
      </div>
    </header>
  );
}