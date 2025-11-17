"use client"
import { FormEvent, useState } from 'react'
import { useRouter } from 'next/navigation'
import Cookies from 'js-cookie'
import { completeProfile } from '@/services/account.service' // Importa el servicio
import { CompleteProfileRequest } from '@/lib/types' // Importa el tipo

// El componente ya no necesita recibir accountId como prop,
// porque la autenticación se maneja a través de la cookie.
export default function CompleteProfilePage() {
  const router = useRouter()
  const [firstName, setFirstName] = useState('')
  const [lastName, setLastName] = useState('')
  const [about, setAbout] = useState('')
  const [photo, setPhoto] = useState('')
  const [latitude, setLatitude] = useState(0)
  const [longitude, setLongitude] = useState(0)
  const [address, setAddress] = useState('')
  const [error, setError] = useState<string | null>(null)

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError(null)

    // 1. Construir el objeto de la petición usando el tipo que definimos
    const profileData: CompleteProfileRequest = {
      profile: {
        firstName,
        lastName,
        photo,
        about,
        location: {
          latitude,
          longitude,
          address
        }
      }
    }

    try {
      // 2. Llamar al servicio centralizado
      const response = await completeProfile(profileData)

      // 3. ¡CRUCIAL! Sobrescribir la cookie con el nuevo token
      // Este nuevo token tiene "profileCompleted: true"
      Cookies.set('jwtToken', response.token, {
        expires: 1, // 1 día
        path: '/'
      })

      console.log('Perfil completado, token actualizado:', response)
      router.replace('/dashboard') // Usamos replace para una mejor UX

    } catch (err) {
      console.error('Error completando perfil:', err)
      setError((err as Error).message || 'Ocurrió un error inesperado.')
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Completa tu Perfil</h2>
      <input value={firstName} onChange={e => setFirstName(e.target.value)} placeholder="Nombre" required />
      <input value={lastName} onChange={e => setLastName(e.target.value)} placeholder="Apellido" required />
      <input value={about} onChange={e => setAbout(e.target.value)} placeholder="Acerca de ti" />
      <input value={photo} onChange={e => setPhoto(e.target.value)} placeholder="URL de foto" />
      <input value={latitude} onChange={e => setLatitude(Number(e.target.value))} placeholder="Latitud" type="number" />
      <input value={longitude} onChange={e => setLongitude(Number(e.target.value))} placeholder="Longitud" type="number" />
      <input value={address} onChange={e => setAddress(e.target.value)} placeholder="Dirección" />
      
      {error && <p style={{ color: 'red' }}>{error}</p>}
      
      <button type="submit">Completar Perfil</button>
    </form>
  )
}
