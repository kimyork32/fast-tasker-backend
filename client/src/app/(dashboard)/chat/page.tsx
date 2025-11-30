"use client";

import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import SockJS from 'sockjs-client';
import { Stomp, CompatClient } from '@stomp/stompjs';
import { ConversationSummary, Message, SendMessageRequest } from '@/lib/types';

const API_URL = "http://localhost:8080/api";
const SOCKET_URL = "http://localhost:8080/ws";

export default function ChatPage() {
  // Estado de la UI
  const [inbox, setInbox] = useState<ConversationSummary[]>([]);
  const [activeChatId, setActiveChatId] = useState<string | null>(null);
  const [messages, setMessages] = useState<Message[]>([]);
  const [inputText, setInputText] = useState("");
  const [currentUserId, setCurrentUserId] = useState<string | null>(null); // Para saber qué mensajes son 'míos'
  const [isConnected, setIsConnected] = useState(false);

  // Referencias (para no perder conexión al re-renderizar)
  const stompClientRef = useRef<CompatClient | null>(null);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  // 1. Cargar Inbox al iniciar
  useEffect(() => {
    fetchInbox();
  }, []);

  // 2. Manejar conexión al seleccionar un chat
  useEffect(() => {
    if (activeChatId) {
      // a. Cargar historial antiguo (REST)
      fetchHistory(activeChatId);
      
      // b. Conectar al Socket (Real-time)
      connectWebSocket(activeChatId);
    }

    // Cleanup: Desconectar si cambiamos de chat o salimos
    return () => {
      disconnectWebSocket();
    };
  }, [activeChatId]);

  // Scroll automático al último mensaje
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // --- FUNCIONES API (REST) ---

  const fetchInbox = async () => {
    try {
      const res = await axios.get<ConversationSummary[]>(`${API_URL}/conversations/inbox`);
      setInbox(res.data);
    } catch (error) {
      console.error("Error cargando inbox:", error);
    }
  };

  const fetchHistory = async (conversationId: string) => {
    try {
      const res = await axios.get<Message[]>(`${API_URL}/conversations/${conversationId}/messages`);
      setMessages(res.data);
      // Inferir el ID del usuario actual si aún no lo tenemos
      if (!currentUserId && res.data.length > 0) {
        setCurrentUserId(res.data[0].senderId);
      }
    } catch (error) {
      console.error("Error cargando historial:", error);
    }
  };

  // --- FUNCIONES WEBSOCKET (STOMP) ---

  const connectWebSocket = (conversationId: string) => {
    // Evitar doble conexión
    if (stompClientRef.current && stompClientRef.current.connected) return;

    const socket = new SockJS(SOCKET_URL);
    const client = Stomp.over(socket);

    // Opcional: Desactivar logs de consola del socket
    // client.debug = () => {}; 

    client.connect({}, () => {
      setIsConnected(true);
      stompClientRef.current = client;

      // SUSCRIPCIÓN DINÁMICA: /topic/conversation.{uuid}
      client.subscribe(`/topic/conversation.${conversationId}`, (payload) => {
        const newMessage: Message = JSON.parse(payload.body);
        
        // Si es el primer mensaje que vemos, podemos usarlo para identificar al usuario actual
        if (!currentUserId) {
          setCurrentUserId(newMessage.senderId);
        }

        // Agregar mensaje recibido al estado
        setMessages((prev) => [...prev, newMessage]);
      });

    }, (error: any) => {
      console.error("Error de conexión STOMP:", error);
      setIsConnected(false);
    });
  };

  const disconnectWebSocket = () => {
    if (stompClientRef.current) {
      stompClientRef.current.disconnect();
      stompClientRef.current = null;
      setIsConnected(false);
    }
  };

  const sendMessage = () => {
    if (!inputText.trim() || !stompClientRef.current || !activeChatId) return;

    // Construir payload exacto como lo espera MessageRequest en Java
    const payload: SendMessageRequest = {
      conversationId: activeChatId,
      content: {
        text: inputText,
        attachmentUrl: null // null si no hay foto
      }
    };

    // ENVIAR: /app/chat.send (Coincide con @MessageMapping)
    stompClientRef.current.send("/app/chat.send", {}, JSON.stringify(payload));
    
    setInputText("");
  };

  // --- RENDERIZADO ---

  return (
    <div className="flex h-full bg-gray-100 -m-6"> {/* Ocupa el 100% del padre y anula el p-6 del layout */}
      
      {/* SIDEBAR (INBOX) */}
      <div className="w-1/3 bg-white border-r border-gray-300 overflow-y-auto">
        <div className="p-4 bg-gray-200 font-bold">Mis Conversaciones</div>
        <ul>
          {inbox.map((chat) => (
            <li 
              key={chat.conversationId}
              onClick={() => setActiveChatId(chat.conversationId)}
              className={`p-4 border-b cursor-pointer hover:bg-gray-50 ${activeChatId === chat.conversationId ? 'bg-blue-50' : ''}`}
            >
              <div className="font-semibold text-gray-800">Usuario: {chat.otherParticipantId.substring(0, 8)}...</div>
              <div className="text-sm text-gray-500 truncate">{chat.lastMessageSnippet}</div>
            </li>
          ))}
        </ul>
        {inbox.length > 0 ? (
          <ul>
            {inbox.map((chat) => (
              <li 
                key={chat.conversationId}
                onClick={() => setActiveChatId(chat.conversationId)}
                className={`p-4 border-b cursor-pointer hover:bg-gray-50 ${activeChatId === chat.conversationId ? 'bg-blue-50' : ''}`}
              >
                <div className="font-semibold text-gray-800">Usuario: {chat.otherParticipantId.substring(0, 8)}...</div>
                <div className="text-sm text-gray-500 truncate">{chat.lastMessageSnippet}</div>
              </li>
            ))}
          </ul>
        ) : (
          <div className="p-4 text-center text-gray-500">
            No tienes chats disponibles
          </div>
        )}
      </div>

      {/* CHAT AREA */}
      <div className="w-2/3 flex flex-col">
        {activeChatId ? (
          <>
            {/* HEADER */}
            <div className="p-4 bg-white border-b flex justify-between items-center shadow-sm">
              <h2 className="font-bold text-lg">Chat ID: {activeChatId.substring(0, 8)}...</h2>
              <div className={`w-3 h-3 rounded-full ${isConnected ? 'bg-green-500' : 'bg-red-500'}`} title={isConnected ? "Conectado" : "Desconectado"}></div>
            </div>

            {/* MENSAJES */}
            <div className="flex-1 overflow-y-auto p-4 space-y-4">
              {messages.map((msg) => {
                const isMe = msg.senderId === currentUserId;
                return (
                  <div key={msg.id} className={`flex ${isMe ? 'justify-end' : 'justify-start'}`}>
                    <div className={`max-w-xs md:max-w-md p-3 rounded-lg shadow ${isMe ? 'bg-blue-600 text-white rounded-br-none' : 'bg-white text-gray-800 rounded-bl-none border'}`}>
                      {/* Renderizar Foto si existe */}
                      {msg.content.attachmentUrl && (
                        <img src={msg.content.attachmentUrl} alt="Adjunto" className="mb-2 rounded-lg max-h-48 object-cover"/>
                      )}
                      
                      {/* Renderizar Texto */}
                      {msg.content.text && <p>{msg.content.text}</p>}
                      
                      <span className={`text-xs block text-right mt-1 ${isMe ? 'text-blue-200' : 'text-gray-400'}`}>
                        {new Date(msg.sentAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                      </span>
                    </div>
                  </div>
                );
              })}
              <div ref={messagesEndRef} />
            </div>

            {/* INPUT AREA */}
            <div className="p-4 bg-white border-t">
              <div className="flex gap-2">
                <input
                  type="text"
                  value={inputText}
                  onChange={(e) => setInputText(e.target.value)}
                  onKeyDown={(e) => e.key === 'Enter' && sendMessage()}
                  placeholder="Escribe un mensaje..."
                  className="flex-1 border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500 text-black"
                />
                <button 
                  onClick={sendMessage}
                  disabled={!isConnected}
                  className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 disabled:bg-gray-400 transition"
                >
                  Enviar
                </button>
              </div>
            </div>
          </>
        ) : (
          <div className="flex-1 flex items-center justify-center text-gray-500">
            Selecciona una conversación para empezar
          </div>
        )}
      </div>
    </div>
  );
}