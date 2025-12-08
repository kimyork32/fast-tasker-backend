// src/services/notification.service.ts
import { apiClient } from '@/lib/apiClient';
import { NotificationResponse } from '@/lib/types';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

let stompClient: Client | null = null;

// La URL de tu endpoint de WebSocket configurado en Spring
const API_PREFIX = '/api/v1/tasker';
const WEBSOCKET_URL = 'http://localhost:8080/ws'; 


/**
 * Conecta al WebSocket y se suscribe al topic de notificaciones del usuario.
 * @param userId - El ID del usuario que ha iniciado sesión.
 * @param onNotification - Una función callback que se ejecutará cada vez que llegue una notificación.
 * @param authToken - El token JWT para autenticar la conexión.
 */

export const getNotifications = (): Promise<NotificationResponse[]> => {
    return apiClient<NotificationResponse[]>(`${API_PREFIX}/notifications`, {
      method: 'GET',
    }, true);
}

export const connectWebSocket = (
  userId: string, 
  onNotification: (notification: NotificationResponse) => void,
  authToken: string
) => {
    console.log("connectWebSocket called");
  if (stompClient && stompClient.active) {
    console.log('WebSocket ya está conectado.');
    return;
  }

  // La forma correcta de instanciar el cliente STOMP con SockJS
  const socket = new SockJS(WEBSOCKET_URL);
  stompClient = new Client();
  stompClient.webSocketFactory = () => socket;
  stompClient.connectHeaders = {
    'Authorization': `Bearer ${authToken}`
  };
  stompClient.debug = (str) => {
    console.log('STOMP: ' + str);
  };
  stompClient.reconnectDelay = 5000;

  // Cuando la conexión es exitosa
  stompClient.onConnect = (frame) => {
    console.log('Conectado al WebSocket: ' + frame);
    stompClient?.subscribe(`/user/topic/notifications`, (message: IMessage) => {

      console.log('DEBUG: Mensaje WebSocket recibido (raw):', message.body);
      const notification: NotificationResponse = JSON.parse(message.body);
      console.log('DEBUG: Notificación procesada (parsed):', notification);
      
      // Llama a la función callback para que la UI pueda reaccionar
      onNotification(notification);
    });
  };

  // Manejo de errores
  stompClient.onStompError = (frame) => {
    console.error('Error en el broker: ' + frame.headers['message']);
    console.error('Detalles: ' + frame.body);
  };

  // Inicia la conexión
  stompClient.activate();
};

/**
 * Desconecta el cliente WebSocket.
 */
export const disconnectWebSocket = () => {
  if (stompClient && stompClient.active) {
    stompClient.deactivate();
    console.log('WebSocket desconectado.');
  }
  stompClient = null;
};
