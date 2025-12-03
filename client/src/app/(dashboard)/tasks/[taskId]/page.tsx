// src/app/(dashboard)/tasks/[taskId]/page.tsx
"use client";

import { useState, useEffect, use } from 'react';
import { 
  getTaskById, 
  createOffer, 
  getOffersByTask 
} from '@/services/task.service';
import { 
  OfferRequest, 
  OfferProfileResponse, 
  TaskCompleteResponse
} from '@/lib/types';
import Link from 'next/link';
import { TaskHeader } from '@/components/task/TaskHeader';
import { TaskPricePanel } from '@/components/task/TaskPricePanel';
import { TaskQuickInfo } from '@/components/task/TaskQuickInfo';
import { TaskDescription } from '@/components/task/TaskDescription';
import { OfferModal } from '@/components/task/OfferModal'
import { TaskTabs } from '@/components/task/TaskTabs'

const mockQuestions = [
  {
    id: '1',
    author: 'Juan Pérez',
    message: 'Hola, ¿necesitas que lleve mis propios productos de limpieza o tú los proporcionas?',
    timestamp: 'Hace 5 horas',
  },
  {
    id: '2',
    author: 'Maria A.',
    message: '¿Hay estacionamiento disponible en el edificio? Tengo una camioneta grande.',
    timestamp: 'Hace 1 día',
    answer: {
      author: 'Poster',
      message: 'Sí, hay estacionamiento de visitas en el sótano 1.',
      timestamp: 'Hace 20 horas',
    },
  },
];


type TaskDetailPageProps = {
  params: Promise<{ taskId: string }>;
};

export default function TaskDetailPage({ params }: TaskDetailPageProps) {
  const { taskId } = use(params); // taskId sigue siendo el ID en bruto de la URL

  const [taskComplete, setTask] = useState<TaskCompleteResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [activeTab, setActiveTab] = useState<'offers' | 'questions'>('questions'); // Empiezo en questions para que lo veas
  
  // Nuevo Estado para el Modal de Oferta
  const [isOfferModalOpen, setIsOfferModalOpen] = useState(false);

  // estados para las ofertas
  const [offers, setOffers] = useState<OfferProfileResponse[]>([]);
  const [isLoadingOffers, setIsLoadingOffers] = useState(false);

  // Estados para el formulario de la oferta
  const [offerPrice, setOfferPrice] = useState<number>(0);
  const [offerMessage, setOfferMessage] = useState('');

  useEffect(() => {
    getTaskById(taskId)
      .then(setTask)
      .catch(console.error)
      .finally(() => setIsLoading(false));
  }, [taskId]);

  // Cargar ofertas cuando la pestaña de ofertas se activa
  useEffect(() => {
    if (activeTab === 'offers' && taskId) {
      setIsLoadingOffers(true);
      getOffersByTask(taskId)
        .then(setOffers)
        .catch(console.error)
        .finally(() => setIsLoadingOffers(false));
    }
  }, [activeTab, taskId]);

  // Setea el precio inicial cuando el modal se abre y la tarea está cargada
  useEffect(() => {
    if (isOfferModalOpen && taskComplete?.task.budget) { // Accede al presupuesto desde taskComplete.task
      setOfferPrice(taskComplete.task.budget);
    }
  }, [isOfferModalOpen, taskComplete]);

  const handleOfferSubmit = async () => {
    if (!taskComplete) return;

    const offerRequest: OfferRequest = {
      price: offerPrice,
      description: offerMessage,
    };

    try {
      const newOffer = await createOffer(taskComplete.task.id, offerRequest); // Pasa taskComplete.task.id
      setOffers(prevOffers => [newOffer, ...prevOffers]); // 1. Actualiza el estado de ofertas
      setActiveTab('offers'); // 2. Cambia a la pestaña de ofertas
      setIsOfferModalOpen(false);
      // Opcional: podrías querer recargar los datos de la tarea para ver la nueva oferta
    } catch (error) {
      console.error("Error al crear la oferta:", error);
      alert("Hubo un error al enviar tu oferta. Inténtalo de nuevo.");
    }
  };

  if (isLoading) return <div className="min-h-screen flex items-center justify-center bg-gray-100"><div className="w-8 h-8 border-4 border-gray-300 border-t-black rounded-full animate-spin"></div></div>;
  if (!taskComplete) return <div className="min-h-screen flex items-center justify-center bg-gray-100 text-gray-500">No se encontró la tarea.</div>;

  return (
    <>
    <div className="min-h-screen bg-gray-100 p-4 md:p-8 font-sans">
      
      {/* Top Navigation */}
      <div className="max-w-7xl mx-auto mb-6 flex justify-between items-center">
        <Link href="/tasks" className="text-sm font-medium text-gray-500 hover:text-black transition flex items-center gap-1">
          ← Volver
        </Link>
        <span className="text-xs font-mono text-gray-400 uppercase tracking-widest">ID: {taskComplete.task?.id.substring(0,8)}...</span> {/* Usa taskComplete.task.id para consistencia */}
      </div>

      {/* GRID LAYOUT PRINCIPAL */}
      <main className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-12 gap-4 md:gap-6">

        {/* 1. BLOQUE TÍTULO */}
        <TaskHeader
          title={taskComplete.task?.title}
          status={taskComplete.task?.status}
          type={taskComplete.task?.type || "Limpieza"} // PONER ETIQUETA
          publishedAgo={"Publicado hace 2 días"} // PONER TIEMPO DE CREACION
          views={23} // PONER VISUALIZACIONES
        />

        {/* 2. BLOQUE PRECIO Y ACCIÓN (Activa el Modal) */}
        <TaskPricePanel
          budget={taskComplete.task?.budget || 0}
          onOpenOfferModal={() => setIsOfferModalOpen(true)}
        />

        {/* 3. BLOQUE INFO RÁPIDA */}
        <TaskQuickInfo
          date={taskComplete.task?.taskDate}
          location={taskComplete.task.location?.address}
        />

        {/* 4. BLOQUE DESCRIPCIÓN */}
        <TaskDescription
          description={taskComplete.task?.description}
          images={taskComplete.task?.images || []} // PONER IMAGEENES EL ATRIBUTO
        />
        
        {/* 5. BLOQUE TABS / ACTIVIDAD (Comentarios y Preguntas) */}
        <TaskTabs
          offers={offers}
          isLoadingOffers={isLoadingOffers}
          questions={mockQuestions}
          numOffers={taskComplete.numOffers}
          numQuestions={taskComplete.numQuestions}
          activeTab={activeTab}
          onTabChange={setActiveTab}
        />


      </main>
    </div>

    {/* =================================================================
        MODAL FLOTANTE DE OFERTA
       ================================================================= */}
    <OfferModal
      isOpen={isOfferModalOpen}
      budget={taskComplete?.task.budget || 0}
      offerPrice={offerPrice}
      offerMessage={offerMessage}
      setOfferPrice={setOfferPrice}
      setOfferMessage={setOfferMessage}
      onClose={() => setIsOfferModalOpen(false)}
      onSubmit={handleOfferSubmit}
    />

    </>
  );
}