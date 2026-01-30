import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EventosService } from '../../services/eventos.service';
import { Evento } from '../../models/evento';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-eventos-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './eventos-form.component.html',
  styleUrl: './eventos-form.component.scss'
})
export class EventosFormComponent implements OnInit {
  eventoForm!: FormGroup;
  eventId: number | null = null;
  isEditing = false;
  loading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private eventoService: EventosService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    // Verificar si es edición o creación
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.eventId = +id;
        this.isEditing = true;
        this.loadEvento(this.eventId);
      }
    });
  }

  private initializeForm(): void {
    this.eventoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      descripcion: ['', [Validators.required, Validators.minLength(10)]],
      fechaEvento: ['', Validators.required],
      horaEvento: ['', Validators.required],
      precioMinimo: ['', [Validators.required, Validators.min(0)]],
      precioMaximo: ['', [Validators.required, Validators.min(0)]],
      localidad: ['', Validators.required],
      genero: ['', Validators.required],
      recinto: ['', Validators.required],
      foto: ['']
    });
  }

  private loadEvento(id: number): void {
    this.loading = true;
    this.eventoService.getEventoById(id).subscribe({
      next: (evento: Evento) => {
        this.eventoForm.patchValue(evento);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar evento:', error);
        this.errorMessage = 'Error al cargar el evento';
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.eventoForm.invalid) {
      return;
    }

    this.loading = true;
    const eventoData: Evento = this.eventoForm.value;

    const request = this.isEditing && this.eventId
      ? this.eventoService.updateEvento(this.eventId, eventoData)
      : this.eventoService.createEvento(eventoData);

    request.subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/eventos']);
      },
      error: (error) => {
        console.error('Error al guardar evento:', error);
        this.errorMessage = 'Error al guardar el evento. Intenta nuevamente.';
        this.loading = false;
      }
    });
  }

  get nombreControl() {
    return this.eventoForm.get('nombre');
  }

  get descripcionControl() {
    return this.eventoForm.get('descripcion');
  }

  get fechaEventoControl() {
    return this.eventoForm.get('fechaEvento');
  }

  get horaEventoControl() {
    return this.eventoForm.get('horaEvento');
  }

  get precioMinimoControl() {
    return this.eventoForm.get('precioMinimo');
  }

  get precioMaximoControl() {
    return this.eventoForm.get('precioMaximo');
  }

  get localidadControl() {
    return this.eventoForm.get('localidad');
  }

  get generoControl() {
    return this.eventoForm.get('genero');
  }

  get recintoControl() {
    return this.eventoForm.get('recinto');
  }

  volver(): void {
    this.router.navigate(['/eventos']);
  }
}
