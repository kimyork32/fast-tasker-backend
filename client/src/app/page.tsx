import Link from "next/link";

export default function LandingPage() {
  return (
    <div>
      <h1>Bienvenido a Fast Tasker</h1>
      <nav>
        <Link href='/login'>Iniciar Sesión</Link>
        <br />
        <Link href='/signup'>Registrarse</Link>
      </nav>
    </div>
  );
}