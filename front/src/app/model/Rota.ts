import { CaminhaoResponse } from "./Caminhao"
import { PontoColetaResponse } from "./PontoColeta"
import { Residuos } from "./Residuos"
import { TrechoRota } from "./Trecho"

export interface RotaResponse{
    id: number,
    nome: string,
    pesoTotal: number,
    dataCriacao: string,
    tipoResiduo: Residuos,
    caminhao: CaminhaoResponse,
    distanciaTotal: number,
    trechos: TrechoRota[],
    pontosColeta: PontoColetaResponse[]
}

export interface RotaRequest{
    nome: string,
    caminhaoPlaca: string,
    tipoResiduoId: number,
    pontosColetaIds: number[]
}