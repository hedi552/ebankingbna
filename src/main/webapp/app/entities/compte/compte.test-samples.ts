import { ICompte, NewCompte } from './compte.model';

export const sampleWithRequiredData: ICompte = {
  id: 9105,
  numcompte: 'chubby',
  agence: 10030,
};

export const sampleWithPartialData: ICompte = {
  id: 22911,
  numcompte: 'fork fellow',
  agence: 16679,
};

export const sampleWithFullData: ICompte = {
  id: 32146,
  numcompte: 'joint mid',
  agence: 13116,
};

export const sampleWithNewData: NewCompte = {
  numcompte: 'impressionable',
  agence: 32715,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
