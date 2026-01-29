import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Evento } from '../models/evento';

@Injectable({
  providedIn: 'root'
})
export class EventosService {

  private baseUrl = 'http://localhost:8081/eventos';

  constructor(private http: HttpClient) { }

  getEventos(): Observable<Evento[]> {
    console.log('Llamando a:', this.baseUrl);
    return this.http.get<Evento[]>(this.baseUrl);
  }
}
