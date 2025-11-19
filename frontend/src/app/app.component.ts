import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CarritoService } from './services/carrito.service';

/**
 * Componente raíz de la aplicación
 *
 * CONCEPTOS ANGULAR:
 * - @Component: Decorador que define un componente
 * - selector: Etiqueta HTML para usar el componente
 * - standalone: true -> Componente independiente (Angular 17+)
 * - imports: Módulos/componentes que usa este componente
 * - template: HTML del componente (inline o templateUrl)
 * - styles: CSS del componente (inline o styleUrls)
 */
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink],
  template: `
    <!-- Header con navegación -->
    <header class="header">
      <div class="container">
        <nav class="nav">
          <a routerLink="/" class="logo">
            <i class="fas fa-leaf"></i>
            <h1>AgroMarket</h1>
          </a>

          <ul class="nav-links">
            <li>
              <a routerLink="/productos">
                <i class="fas fa-store"></i> Productos
              </a>
            </li>
            <li>
              <a routerLink="/carrito" class="carrito-link">
                <i class="fas fa-shopping-cart"></i>
                Carrito
                <span class="badge" *ngIf="totalItems > 0">{{ totalItems }}</span>
              </a>
            </li>
          </ul>
        </nav>
      </div>
    </header>

    <!-- Contenido principal -->
    <main class="main-content">
      <div class="container">
        <!-- router-outlet renderiza el componente de la ruta actual -->
        <router-outlet></router-outlet>
      </div>
    </main>

    <!-- Footer -->
    <footer class="footer">
      <div class="container">
        <p>&copy; 2024 AgroMarket - Proyecto de Aprendizaje Spring Boot + Angular</p>
      </div>
    </footer>
  `,
  styles: [`
    .logo {
      display: flex;
      align-items: center;
      gap: 10px;
      text-decoration: none;
      color: white;
    }

    .logo i {
      font-size: 24px;
    }

    .carrito-link {
      position: relative;
    }

    .carrito-link .badge {
      position: absolute;
      top: -8px;
      right: -12px;
      background: var(--accent-color);
      color: white;
      border-radius: 50%;
      width: 20px;
      height: 20px;
      font-size: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .main-content {
      min-height: calc(100vh - 180px);
      padding: 40px 0;
    }

    .footer {
      background: var(--primary-dark);
      color: white;
      text-align: center;
      padding: 20px 0;
      margin-top: auto;
    }
  `]
})
export class AppComponent {
  totalItems = 0;

  constructor(private carritoService: CarritoService) {
    // Suscribirse a cambios en el carrito
    this.carritoService.carrito$.subscribe(() => {
      this.totalItems = this.carritoService.getTotalItems();
    });
  }
}
