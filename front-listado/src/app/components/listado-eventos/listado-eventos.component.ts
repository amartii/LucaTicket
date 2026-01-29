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

  constructor(private eventosService: EventosService, private cdr: ChangeDetectorRef, private router: Router) { }

  ngOnInit(): void {
    this.cargarEventos();
  }

  toggleEvento(id: number): void {
    this.eventoExpandido = this.eventoExpandido === id ? null : id;
  }

  irAFavoritos(): void {
    this.router.navigate(['/favoritos']);
  }

  irAComprar(): void {
    this.router.navigate(['/compras']);
  }

  cargarEventos(): void {
    this.eventosService.getEventos().subscribe({
      next: (data) => {
        console.log('Datos recibidos:', data);
        console.log('Longitud del array:', data.length);
        this.eventos = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error cargando eventos', err)
    });
  }
}
