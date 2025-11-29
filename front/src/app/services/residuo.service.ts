import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { Residuos } from "../model/Residuos";

@Injectable({
  providedIn: 'root'
})

export class ResiduoService{
    private apiUrl = 'http://localhost:8080/api/tipos-residuo';

    constructor(private http: HttpClient) {}

    findAll(): Observable<Residuos[]> {
        return this.http.get<Residuos[]>(this.apiUrl).pipe(catchError(this.handleError));
    }

    private handleError(error: any) {
      console.error('Erro na API de Puxadores de Produto: ', error);
      return throwError(() => new Error('Erro ao consultar a API de puxadores de produto.'));
    }
}