import { IVirement, NewVirement } from './virement.model';

export const sampleWithRequiredData: IVirement = {
  id: 10771,
  compteBeneficiaire: 'stylish never',
  motif: 'solidly',
};

export const sampleWithPartialData: IVirement = {
  id: 11913,
  compteBeneficiaire: 'however given',
  motif: 'that rim',
};

export const sampleWithFullData: IVirement = {
  id: 18383,
  compteBeneficiaire: 'stigmatize',
  motif: 'segregate',
};

export const sampleWithNewData: NewVirement = {
  compteBeneficiaire: 'rudely waterlogged',
  motif: 'interesting unless',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
