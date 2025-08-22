import { ICompte } from 'app/entities/compte/compte.model';

export interface IMondat {
  id: number;
  compteSrc?: string | null;
  compteBenef?: string | null;
  montant?: number | null;
  code?: string | null;
  compte?: Pick<ICompte, 'id'> | null;
}

export type NewMondat = Omit<IMondat, 'id'> & { id: null };
