// src/app/(dashboard)/tasks/[taskId]/page.tsx
"use client";

import { useState, useEffect, use } from 'react';
import { getTaskById, createOffer, getOffersByTask } from '@/services/task.service'; // Asumiendo que estos servicios se han actualizado para devolver la nueva estructura DTO
import { OfferRequest, OfferProfileResponse, TaskCompleteResponse, TaskResponse, MinimalProfileData, LocationData } from '@/lib/types'; // Añadidos TaskResponse, MinimalProfileData, LocationData
import Link from 'next/link';

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
        <div className="md:col-span-8 bg-white rounded-3xl p-6 md:p-8 shadow-sm border border-gray-200/60 flex flex-col justify-between min-h-[200px]">
          <div>
            <div className="flex gap-3 mb-4">
              <span className={`px-3 py-1 rounded-full text-xs font-bold border ${ // Accede al estado desde taskComplete.task
                taskComplete.task?.status === 'OPEN'
                  ? 'bg-green-50 text-green-700 border-green-200' 
                  : 'bg-gray-50 text-gray-600 border-gray-200'
              }`}>
                {taskComplete.task?.status || 'OPEN'}
              </span>
              <span className="px-3 py-1 rounded-full text-xs font-bold bg-blue-50 text-blue-700 border border-blue-200">
                Limpieza
              </span>
            </div>
            <h1 className="text-3xl md:text-5xl font-extrabold text-gray-900 tracking-tight leading-tight">
              {taskComplete.task?.title} {/* Accede al título desde taskComplete.task */}
            </h1>
          </div>
          <div className="mt-6 flex items-center gap-2 text-gray-400 text-sm">
            <span>Publicado hace 2 días</span>
            <span>•</span>
            <span>23 visualizaciones</span>
          </div>
        </div>

        {/* 2. BLOQUE PRECIO Y ACCIÓN (Activa el Modal) */}
        <div className="md:col-span-4 bg-gray-900 rounded-3xl p-6 md:p-8 shadow-xl text-white flex flex-col justify-between relative overflow-hidden group">
          <div className="absolute top-0 right-0 w-32 h-32 bg-blue-500 rounded-full blur-[60px] opacity-20 group-hover:opacity-40 transition duration-500"></div>

          <div>
            <p className="text-gray-400 text-sm font-medium mb-1">Presupuesto</p>
            <div className="text-4xl md:text-5xl font-bold tracking-tighter">
              <span className="text-2xl text-gray-500 align-top mr-1">S/.</span>
              {taskComplete.task?.budget}
            </div>
          </div>

          <button 
            onClick={() => setIsOfferModalOpen(true)} // <--- AQUI ABRIMOS EL MODAL
            className="mt-8 w-full bg-white text-black font-bold py-4 rounded-xl hover:bg-gray-200 transition-colors flex items-center justify-center gap-2"
          >
            Hacer Oferta
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14 5l7 7m0 0l-7 7m7-7H3" /></svg>
          </button>
        </div>

        {/* 3. BLOQUE INFO RÁPIDA */}
        <div className="md:col-span-4 bg-white rounded-3xl p-6 shadow-sm border border-gray-200/60 space-y-6">
          <div className="flex items-start gap-4">
            <div className="w-10 h-10 rounded-full bg-orange-50 flex items-center justify-center text-orange-500 shrink-0">
               <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
            </div>
            <div>
              <p className="text-xs font-bold text-gray-400 uppercase">Cuándo</p>
              <p className="font-semibold text-gray-900">{taskComplete.task?.taskDate}</p> {/* Accede a taskDate desde taskComplete.task */}
            </div>
          </div>
          {taskComplete.task?.location && ( // Accede a la ubicación desde taskComplete.task
            <div className="flex items-start gap-4">
              <div className="w-10 h-10 rounded-full bg-purple-50 flex items-center justify-center text-purple-500 shrink-0">
                <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" /><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" /></svg>
              </div>
              <div>
                <p className="text-xs font-bold text-gray-400 uppercase">Dónde</p>
                <p className="font-semibold text-gray-900 leading-snug">{taskComplete.task.location.address}</p> {/* Accede a la dirección desde taskComplete.task.location */}
              </div>
            </div>
          )}
        </div>

        {/* 4. BLOQUE DESCRIPCIÓN */}
        <div className="md:col-span-8 bg-white rounded-3xl p-6 md:p-8 shadow-sm border border-gray-200/60">
          <h3 className="text-xl font-bold text-gray-900 mb-4">Detalles de la tarea</h3>
          <div className="prose prose-slate max-w-none text-gray-600 leading-relaxed">
            <p className="whitespace-pre-wrap">{taskComplete.task?.description}</p> {/* Accede a la descripción desde taskComplete.task */}
          </div>
          <div className="mt-8">
            <p className="text-sm font-bold text-gray-900 mb-3">Imágenes de referencia</p>
            <div className="flex gap-2 overflow-x-auto pb-2">
               <div className="w-24 h-24 bg-gray-100 rounded-xl border border-gray-200 flex items-center justify-center text-gray-400 text-xs shrink-0">Foto 1</div>
               <div className="w-24 h-24 bg-gray-100 rounded-xl border border-gray-200 flex items-center justify-center text-gray-400 text-xs shrink-0">Foto 2</div>
            </div>
          </div>
        </div>

        {/* 5. BLOQUE TABS / ACTIVIDAD (Comentarios y Preguntas) */}
        <div className="md:col-span-12 bg-white rounded-3xl p-2 shadow-sm border border-gray-200/60">
            <div className="flex bg-gray-100 p-1 rounded-2xl w-full md:w-fit mb-6">
                <button 
                  onClick={() => setActiveTab('offers')}
                  className={`flex-1 md:flex-none px-6 py-3 rounded-xl text-sm font-bold transition-all ${
                    activeTab === 'offers' ? 'bg-white text-black shadow-sm' : 'text-gray-500 hover:text-gray-700'
                  }`}
            > {/* Accede a numOffers desde taskComplete */}
              Ofertas ({offers.length > 0 ? offers.length : taskComplete.numOffers})
                </button>
                <button 
                  onClick={() => setActiveTab('questions')}
                  className={`flex-1 md:flex-none px-6 py-3 rounded-xl text-sm font-bold transition-all ${
                    activeTab === 'questions' ? 'bg-white text-black shadow-sm' : 'text-gray-500 hover:text-gray-700'
                  }`}
            > {/* Accede a numQuestions desde taskComplete */}
              Preguntas ({taskComplete.numQuestions})
                </button>
            </div>

            <div className="px-6 pb-8 min-h-[200px]">
               {activeTab === 'offers' ? (
                 isLoadingOffers ? (
                   <div className="flex justify-center items-center py-8"><div className="w-6 h-6 border-2 border-gray-200 border-t-black rounded-full animate-spin"></div></div>
                 ) : offers.length > 0 ? (
                   <div className="space-y-6">
                     {offers.filter(op => op && op.offer).map((offerProfile) => (
                       <div key={offerProfile.offer.id.toString()} className="flex gap-4 p-4 border border-gray-100 rounded-2xl">
                         <div className="w-12 h-12 rounded-full bg-gray-200 flex items-center justify-center font-bold text-gray-500 shrink-0">
                           {offerProfile.profile?.photo ? (
                             <img src={offerProfile.profile.photo} alt={`${offerProfile.profile.firstName}`} className="w-full h-full rounded-full object-cover" />
                           ) : (
                             <span>
                               {offerProfile.profile ? `${offerProfile.profile.firstName.charAt(0)}${offerProfile.profile.lastName.charAt(0)}` : '??'}
                             </span>
                           )}
                         </div>
                         <div className="flex-1">
                           <div className="flex justify-between items-start">
                             <div>
                               <p className="font-bold text-gray-900">
                                 {offerProfile.profile ? `${offerProfile.profile.firstName} ${offerProfile.profile.lastName}` : 'Usuario Anónimo'}
                               </p>
                               <p className="text-sm text-gray-500 mt-1">{offerProfile.offer?.description.toString() || 'El ofertante no incluyó un mensaje.'}</p>
                             </div>
                             <div className="text-right shrink-0 pl-4">
                               <p className="text-xl font-bold text-gray-900">S/. {offerProfile.offer?.price || 'N/A'}</p>
                               <p className="text-xs text-gray-400">hace 1h</p>
                             </div>
                           </div>
                         </div>
                       </div>
                     ))}
                   </div>
                 ) : (
                   <div className="flex flex-col items-center justify-center h-full space-y-3 py-8">
                     <div className="w-16 h-16 bg-gray-50 rounded-full flex items-center justify-center border border-gray-100">
                       <svg className="w-6 h-6 text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 12H4" /></svg>
                     </div>
                     <p className="text-gray-500 font-medium">No hay ofertas todavía</p>
                   </div>
                 )
               ) : (
                 // --- AQUÍ ESTÁ LA NUEVA SECCIÓN DE PREGUNTAS TIPO COMENTARIO ---
                 <div className="max-w-3xl">
                   
                    {/* Caja para escribir pregunta */}
                    <div className="flex gap-4 mb-10">
                       <div className="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-bold shrink-0">YO</div>
                       <div className="flex-1">
                          <textarea 
                            rows={3}
                            placeholder="Haz una pregunta pública sobre esta tarea..." 
                            className="w-full border border-gray-200 rounded-xl p-4 text-sm focus:ring-2 focus:ring-black focus:border-transparent outline-none resize-none bg-gray-50"
                          ></textarea>
                          <div className="flex justify-end mt-2">
                             <button className="px-6 py-2 bg-blue-600 text-white text-sm font-bold rounded-full hover:bg-blue-700 transition">
                                Publicar pregunta
                             </button>
                          </div>
                       </div>
                    </div>

                    {/* Lista de Preguntas Existentes (Mockup) */}
                    <div className="space-y-8">
                       
                       {/* Pregunta 1 */}
                       <div className="flex gap-4 group">
                          <div className="w-10 h-10 rounded-full bg-gray-200 flex items-center justify-center text-gray-500 font-bold shrink-0 text-xs">JP</div>
                          <div className="flex-1">
                             <div className="flex items-center gap-2 mb-1">
                                <span className="font-bold text-gray-900 text-sm">Juan Pérez</span>
                                <span className="text-xs text-gray-400">Hace 5 horas</span>
                             </div>
                             <p className="text-gray-700 text-sm leading-relaxed">
                                Hola, ¿necesitas que lleve mis propios productos de limpieza o tú los proporcionas?
                             </p>
                          </div>
                       </div>

                       {/* Pregunta 2 con Respuesta */}
                       <div className="flex gap-4 group">
                          <div className="w-10 h-10 rounded-full bg-purple-100 flex items-center justify-center text-purple-600 font-bold shrink-0 text-xs">MA</div>
                          <div className="flex-1">
                             <div className="flex items-center gap-2 mb-1">
                                <span className="font-bold text-gray-900 text-sm">Maria A.</span>
                                <span className="text-xs text-gray-400">Hace 1 día</span>
                             </div>
                             <p className="text-gray-700 text-sm leading-relaxed">
                                ¿Hay estacionamiento disponible en el edificio? Tengo una camioneta grande.
                             </p>

                             {/* Respuesta del Poster (Anidada) */}
                             <div className="mt-4 flex gap-3 pl-4 border-l-2 border-gray-100">
                                <div className="w-6 h-6 rounded-full bg-black text-white flex items-center justify-center text-[10px] font-bold shrink-0">UP</div>
                                <div>
                                   <div className="flex items-center gap-2 mb-1">
                                      <span className="font-bold text-black text-xs px-1.5 py-0.5 bg-gray-100 rounded">Poster</span>
                                      <span className="text-xs text-gray-400">Hace 20 horas</span>
                                   </div>
                                   <p className="text-gray-600 text-sm">Sí, hay estacionamiento de visitas en el sótano 1.</p>
                                </div>
                             </div>
                          </div>
                       </div>

                    </div>
                 </div>
               )}
            </div>
        </div>

      </main>
    </div>

    {/* =================================================================
        MODAL FLOTANTE DE OFERTA
       ================================================================= */}
    {isOfferModalOpen && (
       <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          {/* Backdrop oscuro */}
          <div 
            className="absolute inset-0 bg-black/60 backdrop-blur-sm transition-opacity"
            onClick={() => setIsOfferModalOpen(false)}
          ></div>

          {/* Contenido del Modal */}
          <div className="relative bg-white rounded-3xl w-full max-w-md p-8 shadow-2xl transform transition-all scale-100">
             <button 
                onClick={() => setIsOfferModalOpen(false)}
                className="absolute top-4 right-4 p-2 text-gray-400 hover:text-gray-900 hover:bg-gray-100 rounded-full transition"
             >
                <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" /></svg>
             </button>

             <h2 className="text-2xl font-bold text-gray-900 mb-1">Haz tu oferta</h2>
             <p className="text-gray-500 text-sm mb-6">El presupuesto del cliente es <span className="font-bold text-gray-900">S/. {taskComplete?.task.budget}</span></p>

             <div className="space-y-4">
                <div>
                   <label className="block text-xs font-bold text-gray-700 uppercase mb-2">Tu precio (S/.)</label>
                   <input 
                      type="number" 
                      value={offerPrice}
                      onChange={(e) => setOfferPrice(Number(e.target.value))}
                      className="w-full text-3xl font-bold border-b-2 border-gray-200 focus:border-black outline-none py-2 px-1 text-gray-900"
                   />
                </div>

                <div>
                   <label className="block text-xs font-bold text-gray-700 uppercase mb-2 mt-4">Mensaje para el cliente</label>
                   <textarea 
                      value={offerMessage} // Vinculado al estado correcto
                      onChange={(e) => setOfferMessage(e.target.value)} // Actualiza el estado correcto
                      className="w-full border border-gray-300 rounded-xl p-3 text-sm focus:ring-2 focus:ring-black focus:border-transparent outline-none min-h-[100px]"
                      placeholder="Hola, me gustaría ayudarte con esto. Tengo experiencia en..."
                   ></textarea>
                </div>

                <div className="pt-4">
                   <button 
                      className="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-4 rounded-xl shadow-lg shadow-green-900/10 transition transform active:scale-[0.98]"
                      onClick={handleOfferSubmit}
                   >
                      Enviar Oferta
                   </button>
                   <p className="text-center text-xs text-gray-400 mt-3">Sin compromiso hasta que te acepten.</p>
                </div>
             </div>
          </div>
       </div>
    )}
    </>
  );
}