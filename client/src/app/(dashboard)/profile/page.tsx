"use client";

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Account } from '@/lib/types'; // ¡Tipo actualizado!
import { getMyProfile } from '@/services/account.service'; // ¡Servicio actualizado!

export default function Profile() {
  const [profile, setProfile] = useState<Account | null>(null); // Tipado
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        // 4. Usar el servicio (que ya usa apiClient y credentials: 'include')
        const data = await getMyProfile();
        setProfile(data);
      } catch (err) {
        setError((err as Error).message);
        setTimeout(() => router.push('/login'), 2000);
      } finally {
        setIsLoading(false);
      }
    };

    fetchProfile();
  }, [router]);

  if (isLoading) return <div>Cargando perfil...</div>;
  if (error) return <div style={{ color: 'red' }}>Error: {error}</div>;

  return (
    <div>
      <h1>Mi Perfil</h1>
      
      {/* 6. Renderizar con los nuevos campos del DTO */}
      {profile ? (
        <ul>
          <li><strong>ID (UUID):</strong> {profile.id}</li>
          <li><strong>Email:</strong> {profile.email}</li>
          <li><strong>Estado:</strong> {profile.status}</li>
        </ul>
      ) : (
        <p>No se pudieron cargar los datos del perfil.</p>
      )}

      <h3>Datos completos (JSON):</h3>
      <pre>
        {JSON.stringify(profile, null, 2)}
      </pre>
    </div>
  );
}