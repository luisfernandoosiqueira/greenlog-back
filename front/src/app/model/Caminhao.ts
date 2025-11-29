import { StatusCaminhao } from "./enums/StatusCaminhao";
import { MotoristaResponse } from "./Motorista";
import { Residuos } from "./Residuos";

export interface CaminhaoResponse{
    placa: string,
    motorista: MotoristaResponse,
    capacidade: number,
    status: StatusCaminhao
    tiposResiduo: Residuos[]
}

export interface CaminhaoRequest{
    placa: string,
    motorista: MotoristaResponse,
    capacidade: number,
    status: StatusCaminhao,
    tiposResiduo: Residuos[]
}