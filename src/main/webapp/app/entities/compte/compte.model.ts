import { IUser } from 'app/entities/user/user.model';

export interface ICompte {
  id: number;
  numcompte?: string | null;
  agence?: number | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewCompte = Omit<ICompte, 'id'> & { id: null };
