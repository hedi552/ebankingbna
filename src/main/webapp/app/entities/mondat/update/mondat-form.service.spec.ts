import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../mondat.test-samples';

import { MondatFormService } from './mondat-form.service';

describe('Mondat Form Service', () => {
  let service: MondatFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MondatFormService);
  });

  describe('Service methods', () => {
    describe('createMondatFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMondatFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            compteSrc: expect.any(Object),
            compteBenef: expect.any(Object),
            montant: expect.any(Object),
            code: expect.any(Object),
            compte: expect.any(Object),
          }),
        );
      });

      it('passing IMondat should create a new form with FormGroup', () => {
        const formGroup = service.createMondatFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            compteSrc: expect.any(Object),
            compteBenef: expect.any(Object),
            montant: expect.any(Object),
            code: expect.any(Object),
            compte: expect.any(Object),
          }),
        );
      });
    });

    describe('getMondat', () => {
      it('should return NewMondat for default Mondat initial value', () => {
        const formGroup = service.createMondatFormGroup(sampleWithNewData);

        const mondat = service.getMondat(formGroup) as any;

        expect(mondat).toMatchObject(sampleWithNewData);
      });

      it('should return NewMondat for empty Mondat initial value', () => {
        const formGroup = service.createMondatFormGroup();

        const mondat = service.getMondat(formGroup) as any;

        expect(mondat).toMatchObject({});
      });

      it('should return IMondat', () => {
        const formGroup = service.createMondatFormGroup(sampleWithRequiredData);

        const mondat = service.getMondat(formGroup) as any;

        expect(mondat).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMondat should not enable id FormControl', () => {
        const formGroup = service.createMondatFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMondat should disable id FormControl', () => {
        const formGroup = service.createMondatFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
