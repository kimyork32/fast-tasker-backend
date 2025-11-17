'use client'; 

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link'; // Importar Link
import { login } from '@/services/account.service'; // Servicio actualizado
import { LoginRequest } from '@/lib/types'; // DTO actualizado
import Cookies from 'js-cookie';

export default function LoginPage() {
  // --- ESTOS ESTADOS SON LA CLAVE ---
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null); // Limpiar errores previos

    try {
      // 0. Construir el DTO con los datos de los 'useState'
      const loginData: LoginRequest = { 
        email: email, 
        rawPassword: password 
      };
      
      // 2. Llamar al servicio
      // El backend establecerá la cookie HttpOnly si es exitoso
      const loginResponse = await login(loginData); 
      
      // 2.2 guardar token de la respuesta en cookie
      // middleware podrá leer esta cookie
      Cookies.set('jwtToken', loginResponse.token, {
        expires: 1, // cookie expira en 1 dia
        path: '/' // cookie disponible en todo el sitio
        // secure: process.env.NODE_ENV == 'production' // opcional si se envia por HTTPS en produccion
      }); 

      console.log('Login exitoso, token guardado. Redirigiendo a /dashboard...');

      // 3. Redirigir al dashboard
      // router.push refresca la página, lo que
      // hace que el proxy/middleware se ejecute con la nueva cookie.
      router.push('/dashboard'); 

    } catch (err) {
      // Si el servicio 'login' falla (ej. 401 Unauthorized),
      // el 'apiClient' lanzará un error que atrapamos aquí.
      console.error(err);
      setError((err as Error).message);
    }
  };

  return (
    <div>
      <h1>Iniciar Sesión</h1>
      
      {/* --- ESTE ES EL FORMULARIO QUE DEBES TENER --- */}
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="email">Email:</label>
          <input
            id="email"
            type="email"
            value={email} // Conectado al estado
            onChange={(e) => setEmail(e.target.value)} // Actualiza el estado
            required
          />
        </div>
        <div>
          <label htmlFor="password">Contraseña:</label>
          <input
            id="password"
            type="password"
            value={password} // Conectado al estado
            onChange={(e) => setPassword(e.target.value)} // Actualiza el estado
            required
          />
        </div>
        <button type="submit">Entrar</button>
      </form>
      {/* --- FIN DEL FORMULARIO --- */}

      {error && (
        <p style={{ color: 'red' }}>
          Error: {error}
        </p>
      )}
      <p>
        ¿No tienes cuenta? <Link href="/register">Regístrate</Link>
      </p>
    </div>
  );
}