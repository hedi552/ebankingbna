import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../virement.test-samples';

import { VirementFormService } from './virement-form.service';

describe('Virement Form Service', () => {
  let service: VirementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VirementFormService);
  });

  describe('Service methods', () => {
    describe('createVirementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVirementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            compteBeneficiaire: expect.any(Object),
            motif: expect.any(Object),
            compte: expect.any(Object),
            beneficiaire: expect.any(Object),
          }),
        );
      });

      it('passing IVirement should create a new form with FormGroup', () => {
        const formGroup = service.createVirementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            compteBeneficiaire: expect.any(Object),
            motif: expect.any(Object),
            compte: expect.any(Object),
            beneficiaire: expect.any(Object),
          }),
        );
      });
    });

    describe('getVirement', () => {
      it('should return NewVirement for default Virement initial value', () => {
        const formGroup = service.createVirementFormGroup(sampleWithNewData);

        const virement = service.getVirement(formGroup) as any;

        expect(virement).toMatchObject(sampleWithNewData);
      });

      it('should return NewVirement for empty Virement initial value', () => {
        const formGroup = service.createVirementFormGroup();

        const virement = service.getVirement(formGroup) as any;

        expect(virement).toMatchObject({});
      });

      it('should return IVirement', () => {
        const formGroup = service.createVirementFormGroup(sampleWithRequiredData);

        const virement = service.getVirement(formGroup) as any;

        expect(virement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVirement should not enable id FormControl', () => {
        const formGroup = service.createVirementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVirement should disable id FormControl', () => {
        const formGroup = service.createVirementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
