import { BairroResponse } from "./Bairro";
import { Residuos } from "./Residuos";

export interface PontoColetaResponse{
    id: number,
    bairro: BairroResponse,
    nome: string,
    responsavel: string,
    telefone: string,
    email: string,
    endereco: string,
    horaEntrada: string;
    horaSaida: string;
    quantidadeResiduosKg: number,
    tipoResiduo: Residuos[]
}

export interface PontoColetaRequest{
    bairroId: number,
    nome: string,
    responsavel: string,
    telefone: string,
    email: string,
    endereco: string,
    horaEntrada: string,
    horaSaida: string,
    quantidadeResiduosKg: number,
    tipoResiduoId: number[]
}