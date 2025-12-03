"use client";

interface TaskHeaderProps {
  title: string;
  status: string;
  type: string; // “Limpieza” en tu ejemplo
  publishedAgo: string; // “Publicado hace 2 días”
  views: number;
}

export function TaskHeader({
  title,
  status,
  type,
  publishedAgo,
  views,
}: TaskHeaderProps) {
  return (
    <div className="md:col-span-8 bg-white rounded-3xl p-6 md:p-8 shadow-sm border border-gray-200/60 flex flex-col justify-between min-h-[200px]">
      <div>
        {/* Labels de estado y tipo */}
        <div className="flex gap-3 mb-4">
          <span
            className={`px-3 py-1 rounded-full text-xs font-bold border ${
              status === "OPEN"
                ? "bg-green-50 text-green-700 border-green-200"
                : "bg-gray-50 text-gray-600 border-gray-200"
            }`}
          >
            {status}
          </span>

          <span className="px-3 py-1 rounded-full text-xs font-bold bg-blue-50 text-blue-700 border border-blue-200">
            {type}
          </span>
        </div>

        {/* Título */}
        <h1 className="text-3xl md:text-5xl font-extrabold text-gray-900 tracking-tight leading-tight">
          {title}
        </h1>
      </div>

      {/* Publicado + vistas */}
      <div className="mt-6 flex items-center gap-2 text-gray-400 text-sm">
        <span>{publishedAgo}</span>
        <span>•</span>
        <span>{views} visualizaciones</span>
      </div>
    </div>
  );
}
