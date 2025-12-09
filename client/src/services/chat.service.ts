import { apiClient } from '@/lib/apiClient';
import { ConversationSummary, Message, SendMessageRequest } from '@/lib/types';
import { Stomp, CompatClient } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const API_PREFIX = '/api/v1/conversations';
const SOCKET_URL = "http://localhost:8080/ws";

let stompClient: CompatClient | null = null;

/**
 * Obtiene el inbox (lista de conversaciones) del usuario.
 */
export const getInbox = (): Promise<ConversationSummary[]> => {
  return apiClient<ConversationSummary[]>(`${API_PREFIX}/inbox`, {
    method: 'GET',
  }, true);
};

/**
 * Obtiene el historial de mensajes de una conversación específica.
 */
export const getMessages = (conversationId: string): Promise<Message[]> => {
  return apiClient<Message[]>(`/api/v1/conversations/${conversationId}/messages`, {
    method: 'GET',
  }, true);
};

/**
 * Conecta al WebSocket y se suscribe a los mensajes de una conversación.
 * @param conversationId - El ID de la conversación a la que suscribirse.
 * @param onMessageReceived - Callback que se ejecuta cuando se recibe un nuevo mensaje.
 * @param onConnectionChange - Callback para notificar cambios en el estado de la conexión.
 */
export const connect = (
  conversationId: string,
  onMessageReceived: (message: Message) => void,
  onConnectionChange: (isConnected: boolean) => void
): void => {
  if (stompClient && stompClient.connected) {
    return;
  }

  const socket = new SockJS(SOCKET_URL);
  stompClient = Stomp.over(socket);

  // Desactivar logs de STOMP en la consola
  stompClient.debug = () => {};
  
  const token = document.cookie.split(';').map(c => c.trim()).find(c => c.startsWith('jwtToken='))?.split('=')[1];
  const headers = {
    Authorization: `Bearer ${token}`,
  };


  stompClient.connect(headers, () => {
    onConnectionChange(true);
    stompClient?.subscribe(`/topic/conversation.${conversationId}`, (payload) => {
      console.log("Nuevo mensaje recibido del WebSocket:", payload.body);
      const newMessage: Message = JSON.parse(payload.body);
      onMessageReceived(newMessage);
    });
  }, (error: any) => {
    console.error("Error de conexión STOMP:", error);
    onConnectionChange(false);
  });
};

/**
 * Desconecta del WebSocket.
 */
export const disconnect = (): void => {
  if (stompClient) {
    stompClient.disconnect();
    stompClient = null;
  }
};

/**
 * Envía un mensaje a través del WebSocket.
 */
export const sendMessage = (payload: SendMessageRequest): void => {

  console.log("sendMessage called");
  
  if (!stompClient || !stompClient.connected) return;
  stompClient.send("/app/chat/.send", {}, JSON.stringify(payload));
};