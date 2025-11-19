import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../services/producto.service';
import { Producto, ProductoRequest } from '../models/producto.model';

/**
 * ========================================
 * COMPONENTE - LISTA DE PRODUCTOS
 * ========================================
 *
 * Un componente en Angular tiene:
 * - Template (HTML): Vista
 * - Clase (TypeScript): Lógica
 * - Estilos (CSS): Presentación
 *
 * @Component define:
 * - selector: etiqueta HTML para usar el componente
 * - standalone: componente independiente (Angular 14+)
 * - imports: módulos que necesita
 * - template: HTML del componente
 */
@Component({
  selector: 'app-producto-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <h2>Gestión de Productos</h2>

      <!-- Formulario de búsqueda -->
      <div class="search-box">
        <input
          type="text"
          [(ngModel)]="searchTerm"
          placeholder="Buscar producto..."
          (keyup.enter)="buscar()"
        />
        <button (click)="buscar()">Buscar</button>
        <button (click)="cargarProductos()">Ver Todos</button>
      </div>

      <!-- Formulario para crear/editar -->
      <div class="form-box">
        <h3>{{ editando ? 'Editar' : 'Nuevo' }} Producto</h3>
        <form (ngSubmit)="guardar()">
          <div class="form-group">
            <label>Nombre:</label>
            <input type="text" [(ngModel)]="productoForm.nombre" name="nombre" required />
          </div>
          <div class="form-group">
            <label>Descripción:</label>
            <textarea [(ngModel)]="productoForm.descripcion" name="descripcion"></textarea>
          </div>
          <div class="form-group">
            <label>Precio:</label>
            <input type="number" [(ngModel)]="productoForm.precio" name="precio" required min="0" step="0.01" />
          </div>
          <div class="form-group">
            <label>Stock:</label>
            <input type="number" [(ngModel)]="productoForm.stock" name="stock" required min="0" />
          </div>
          <div class="form-actions">
            <button type="submit">{{ editando ? 'Actualizar' : 'Crear' }}</button>
            <button type="button" (click)="cancelar()" *ngIf="editando">Cancelar</button>
          </div>
        </form>
      </div>

      <!-- Mensaje de estado -->
      <div class="message" *ngIf="mensaje" [class.error]="error">
        {{ mensaje }}
      </div>

      <!-- Lista de productos -->
      <div class="product-list">
        <h3>Productos ({{ productos.length }})</h3>
        <table *ngIf="productos.length > 0">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Precio</th>
              <th>Stock</th>
              <th>Activo</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <!-- *ngFor: directiva para iterar arrays -->
            <tr *ngFor="let producto of productos" [class.inactive]="!producto.activo">
              <td>{{ producto.id }}</td>
              <td>{{ producto.nombre }}</td>
              <!-- Pipe currency: formatea como moneda -->
              <td>{{ producto.precio | currency:'USD' }}</td>
              <td [class.low-stock]="producto.stock < 10">{{ producto.stock }}</td>
              <td>{{ producto.activo ? 'Sí' : 'No' }}</td>
              <td>
                <button (click)="editar(producto)">Editar</button>
                <button (click)="eliminar(producto.id!)" class="danger">Eliminar</button>
              </td>
            </tr>
          </tbody>
        </table>
        <p *ngIf="productos.length === 0">No hay productos</p>
      </div>
    </div>
  `,
  styles: [`
    .container {
      max-width: 1000px;
      margin: 0 auto;
      padding: 20px;
      font-family: Arial, sans-serif;
    }
    .search-box, .form-box {
      margin-bottom: 20px;
      padding: 15px;
      background: #f5f5f5;
      border-radius: 5px;
    }
    .search-box input {
      padding: 8px;
      margin-right: 10px;
      width: 300px;
    }
    .form-group {
      margin-bottom: 10px;
    }
    .form-group label {
      display: block;
      margin-bottom: 5px;
      font-weight: bold;
    }
    .form-group input, .form-group textarea {
      width: 100%;
      padding: 8px;
      box-sizing: border-box;
    }
    .form-actions {
      margin-top: 15px;
    }
    button {
      padding: 8px 16px;
      margin-right: 10px;
      cursor: pointer;
      background: #007bff;
      color: white;
      border: none;
      border-radius: 4px;
    }
    button:hover {
      background: #0056b3;
    }
    button.danger {
      background: #dc3545;
    }
    button.danger:hover {
      background: #c82333;
    }
    table {
      width: 100%;
      border-collapse: collapse;
    }
    th, td {
      padding: 10px;
      text-align: left;
      border-bottom: 1px solid #ddd;
    }
    th {
      background: #f8f9fa;
    }
    .inactive {
      opacity: 0.6;
    }
    .low-stock {
      color: #dc3545;
      font-weight: bold;
    }
    .message {
      padding: 10px;
      margin-bottom: 20px;
      background: #d4edda;
      border-radius: 4px;
      color: #155724;
    }
    .message.error {
      background: #f8d7da;
      color: #721c24;
    }
  `]
})
export class ProductoListComponent implements OnInit {
  // Propiedades del componente
  productos: Producto[] = [];
  searchTerm: string = '';
  mensaje: string = '';
  error: boolean = false;
  editando: boolean = false;
  productoEditandoId: number | null = null;

  // Formulario (two-way binding con ngModel)
  productoForm: ProductoRequest = {
    nombre: '',
    descripcion: '',
    precio: 0,
    stock: 0,
    activo: true
  };

  /**
   * Inyección del servicio
   */
  constructor(private productoService: ProductoService) { }

  /**
   * ngOnInit: Lifecycle hook
   * Se ejecuta cuando el componente se inicializa
   */
  ngOnInit(): void {
    this.cargarProductos();
  }

  /**
   * Cargar todos los productos
   */
  cargarProductos(): void {
    this.productoService.obtenerTodos().subscribe({
      next: (data) => {
        this.productos = data;
        this.searchTerm = '';
      },
      error: (err) => {
        this.mostrarMensaje('Error al cargar productos', true);
        console.error(err);
      }
    });
  }

  /**
   * Buscar productos
   */
  buscar(): void {
    if (!this.searchTerm.trim()) {
      this.cargarProductos();
      return;
    }

    this.productoService.buscarPorNombre(this.searchTerm).subscribe({
      next: (data) => this.productos = data,
      error: (err) => {
        this.mostrarMensaje('Error en la búsqueda', true);
        console.error(err);
      }
    });
  }

  /**
   * Guardar producto (crear o actualizar)
   */
  guardar(): void {
    if (!this.productoForm.nombre || this.productoForm.precio <= 0) {
      this.mostrarMensaje('Complete los campos requeridos', true);
      return;
    }

    if (this.editando && this.productoEditandoId) {
      // Actualizar
      this.productoService.actualizar(this.productoEditandoId, this.productoForm)
        .subscribe({
          next: () => {
            this.mostrarMensaje('Producto actualizado');
            this.resetForm();
            this.cargarProductos();
          },
          error: (err) => {
            this.mostrarMensaje('Error al actualizar', true);
            console.error(err);
          }
        });
    } else {
      // Crear
      this.productoService.crear(this.productoForm).subscribe({
        next: () => {
          this.mostrarMensaje('Producto creado');
          this.resetForm();
          this.cargarProductos();
        },
        error: (err) => {
          this.mostrarMensaje('Error al crear producto', true);
          console.error(err);
        }
      });
    }
  }

  /**
   * Editar producto
   */
  editar(producto: Producto): void {
    this.editando = true;
    this.productoEditandoId = producto.id!;
    this.productoForm = {
      nombre: producto.nombre,
      descripcion: producto.descripcion,
      precio: producto.precio,
      stock: producto.stock,
      activo: producto.activo
    };
  }

  /**
   * Eliminar producto
   */
  eliminar(id: number): void {
    if (confirm('¿Está seguro de eliminar este producto?')) {
      this.productoService.eliminar(id).subscribe({
        next: () => {
          this.mostrarMensaje('Producto eliminado');
          this.cargarProductos();
        },
        error: (err) => {
          this.mostrarMensaje('Error al eliminar', true);
          console.error(err);
        }
      });
    }
  }

  /**
   * Cancelar edición
   */
  cancelar(): void {
    this.resetForm();
  }

  /**
   * Resetear formulario
   */
  private resetForm(): void {
    this.editando = false;
    this.productoEditandoId = null;
    this.productoForm = {
      nombre: '',
      descripcion: '',
      precio: 0,
      stock: 0,
      activo: true
    };
  }

  /**
   * Mostrar mensaje temporal
   */
  private mostrarMensaje(msg: string, esError: boolean = false): void {
    this.mensaje = msg;
    this.error = esError;
    setTimeout(() => {
      this.mensaje = '';
      this.error = false;
    }, 3000);
  }
}
