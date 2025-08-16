import { ICompte } from 'app/entities/compte/compte.model';

export interface IVirement {
  id: number;
  compteBeneficiaire?: string | null;
  motif?: string | null;
  compte?: Pick<ICompte, 'id'> | null;
  beneficiaire?: Pick<ICompte, 'id'> | null;
}

export type NewVirement = Omit<IVirement, 'id'> & { id: null };
