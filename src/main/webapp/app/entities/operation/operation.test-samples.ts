import { IOperation, NewOperation } from './operation.model';

export const sampleWithRequiredData: IOperation = {
  id: 6430,
  montant: 5529,
  libelle: 'sonar unwieldy',
};

export const sampleWithPartialData: IOperation = {
  id: 3157,
  montant: 1069,
  libelle: 'loftily phooey but',
};

export const sampleWithFullData: IOperation = {
  id: 13289,
  montant: 21684,
  libelle: 'yowza',
};

export const sampleWithNewData: NewOperation = {
  montant: 25417,
  libelle: 'smoothly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
