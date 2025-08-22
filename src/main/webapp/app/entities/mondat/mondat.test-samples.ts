import { IMondat, NewMondat } from './mondat.model';

export const sampleWithRequiredData: IMondat = {
  id: 8691,
  compteSrc: 'astride',
  compteBenef: 'jubilantly softly without',
  montant: 30399.67,
};

export const sampleWithPartialData: IMondat = {
  id: 13852,
  compteSrc: 'so account',
  compteBenef: 'at hovel frenetically',
  montant: 24267.29,
  code: 'apostrophize however limited',
};

export const sampleWithFullData: IMondat = {
  id: 1800,
  compteSrc: 'unaccountably furiously membership',
  compteBenef: 'bleakly opposite brightly',
  montant: 12700.7,
  code: 'lively',
};

export const sampleWithNewData: NewMondat = {
  compteSrc: 'how behind worthwhile',
  compteBenef: 'bah before',
  montant: 24974.64,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
