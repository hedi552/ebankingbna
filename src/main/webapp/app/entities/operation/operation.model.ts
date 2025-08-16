import { ICompte } from 'app/entities/compte/compte.model';

export interface IOperation {
  id: number;
  montant?: number | null;
  libelle?: string | null;
  compte?: Pick<ICompte, 'id'> | null;
}

export type NewOperation = Omit<IOperation, 'id'> & { id: null };
