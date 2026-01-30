import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { EventosService } from '../../services/eventos.service';
import { Evento } from '../../models/evento';

@Component({
  selector: 'app-listado-eventos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './listado-eventos.component.html',
  styleUrl: './listado-eventos.component.scss'
})
export class ListadoEventosComponent implements OnInit {

  eventos: Evento[] = [];
  eventoExpandido: number | null = null;
  loading = false;
  errorMessage = '';

  constructor(private eventosService: EventosService, private router: Router, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.cargarEventos();
  }

  toggleEvento(id: number): void {
    this.eventoExpandido = this.eventoExpandido === id ? null : id;
  }

  getImagenEvento(id: number): string {
    // Mapea el ID del evento a su imagen correspondiente (evento01.png a evento12.png)
    const numeroEvento = String(id).padStart(2, '0');
    return `assets/eventos/evento${numeroEvento}.png`;
  }

  irAFavoritos(): void {
    this.router.navigate(['/favoritos']);
  }

  irAComprar(): void {
    this.router.navigate(['/compras']);
  }

  crearEvento(): void {
    this.router.navigate(['/eventos/nuevo']);
  }

  editarEvento(id: number, event: Event): void {
    event.stopPropagation();
    this.router.navigate(['/eventos', id, 'editar']);
  }

  borrarEvento(id: number, event: Event): void {
    event.stopPropagation();
    const evento = this.eventos.find(e => e.id === id);
    if (evento && confirm(`¿Estás seguro de que deseas eliminar "${evento.nombre}"?`)) {
      this.eventosService.deleteEvento(id).subscribe({
        next: () => {
          this.cargarEventos();
        },
        error: (err) => {
          console.error('Error al borrar evento', err);
          this.errorMessage = 'Error al borrar el evento. Intenta nuevamente.';
        }
      });
    }
  }

  cargarEventos(): void {
    this.loading = true;
    this.errorMessage = '';
    this.eventosService.getEventos().subscribe({
      next: (data) => {
        this.eventos = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error cargando eventos', err);
        this.errorMessage = 'Error al cargar los eventos. Intenta nuevamente.';
        this.loading = false;
      }
    });
  }
}

