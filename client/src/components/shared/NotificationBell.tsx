// src/components/notifications/NotificationBell.tsx
'use client';

import { useState, useEffect } from 'react';
import { Bell } from 'lucide-react';
import {
  getNotifications,
  connectWebSocket,
  disconnectWebSocket,
} from '@/services/notification.service';
import { NotificationResponse } from '@/lib/types';
import Cookies from 'js-cookie';
import { jwtDecode } from 'jwt-decode';
import { Button } from '@/components/ui/button';

export function NotificationBell() {
  const [notifications, setNotifications] = useState<NotificationResponse[]>([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [isOpen, setIsOpen] = useState(false);
  const [userId, setUserId] = useState<string | null>(null);

  // Obtener userId y token para las notificaciones
  useEffect(() => {
    const token = Cookies.get('jwtToken');
    if (token) {
      try {
        const decodedToken: { sub: string } = jwtDecode(token);
        const userIdFromToken = decodedToken.sub;
        setUserId(userIdFromToken);

        // Conectar al WebSocket
        connectWebSocket(userIdFromToken, (newNotification) => {
          setNotifications(prev => [newNotification, ...prev]);
          setUnreadCount(prev => prev + 1);
        }, token);

      } catch (error) {
        console.error("Error al decodificar token o conectar a WebSocket:", error);
      }
    }

    // Desconectar al desmontar el componente
    return () => {
      disconnectWebSocket();
    };
  }, []);

  // Cargar notificaciones iniciales
  useEffect(() => {
    if (userId) {
      getNotifications()
        .then(data => {
          setNotifications(data);
          // Aquí puedes añadir lógica para contar solo las no leídas si tu API lo permite
          setUnreadCount(data.length); 
        })
        .catch(console.error);
    }
  }, [userId]);

  return (
    <div className="relative">
      <Button variant="ghost" size="icon" onClick={() => setIsOpen(!isOpen)}>
        <Bell className="h-5 w-5" />
        {unreadCount > 0 && (
          <span className="absolute top-0 right-0 block h-4 w-4 rounded-full bg-red-500 text-white text-[10px] flex items-center justify-center ring-2 ring-white">
            {unreadCount}
          </span>
        )}
      </Button>

      {isOpen && (
        <div className="absolute right-0 mt-2 w-80 bg-white rounded-lg shadow-lg border z-10">
          <div className="p-3 font-bold border-b">Notificaciones</div>
          <div className="max-h-96 overflow-y-auto">
            {notifications.length > 0 ? (
              notifications.map(notif => (
                <div key={notif.id} className="p-3 border-b hover:bg-gray-50 cursor-pointer">
                  <p className="text-sm">
                    {
                      // Construye un mensaje basado en el tipo de notificación
                      notif.type === 'OFFER_ACCEPTED'
                        ? `¡Tu oferta para la tarea ha sido aceptada!`
                        : notif.type === 'NEW_MESSAGE'
                        ? `Has recibido un nuevo mensaje.`
                        : `Nueva notificación (${notif.type})`
                    }
                  </p>
                  <p className="text-xs text-gray-400 mt-1">
                    {/* Asegúrate de que el nombre de la propiedad coincida con el backend ('createAt') */}
                    {new Date(notif.createAt).toLocaleString('es-ES', {
                      day: 'numeric', month: 'short', hour: 'numeric', minute: '2-digit'
                    })}
                  </p>
                </div>
              ))
            ) : (
              <p className="p-4 text-sm text-gray-500">No tienes notificaciones.</p>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
