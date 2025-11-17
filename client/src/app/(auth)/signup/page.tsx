'use client';

import { useState, FormEvent } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import Cookies from 'js-cookie';

// Importa el servicio y los tipos necesarios
import { register } from '@/services/account.service';
import { SignupRequest, SignupResponse } from '@/lib/types';

export default function RegisterPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError(null); // Limpia errores anteriores

    try {
      // 1. Construye el objeto con los datos del formulario
      const registerData: SignupRequest = {
        email: email,
        rawPassword: password,
      };

      // 2. Llama al servicio de registro
      const registerResponse: SignupResponse = await register(registerData);

      // 3. ¡Clave! Guarda el token de la respuesta en una cookie
      // Esto permite que el middleware te reconozca como un usuario recién registrado.
      Cookies.set('jwtToken', registerResponse.token, {
        expires: 1, // La cookie expira en 1 día
        path: '/',  // Disponible en todo el sitio
      });

      console.log('Registro exitoso, token guardado. Redirigiendo para completar perfil...');

      // 4. Redirige al usuario para que complete su perfil
      // Usamos 'replace' para que no pueda volver a la página de registro con el botón "atrás"
      router.replace('/complete-profile');

    } catch (err) {
      // Si el servicio falla, el apiClient lanzará un error que atrapamos aquí
      console.error('Fallo en el registro:', err);
      setError((err as Error).message);
    }
  };

  return (
    <div>
      <h1>Crea tu cuenta</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="email">Email</label>
          <input
            id="email"
            name="email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="password">Contraseña</label>
          <input
            id="password"
            name="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Registrarse</button>
      </form>

      {/* Muestra el mensaje de error si existe */}
      {error && <p style={{ color: 'red' }}>Error: {error}</p>}

      <p>
        ¿Ya tienes una cuenta? <Link href="/login">Inicia sesión</Link>
      </p>
    </div>
  );
}
