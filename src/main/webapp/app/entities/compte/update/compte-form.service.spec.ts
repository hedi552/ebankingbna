import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../compte.test-samples';

import { CompteFormService } from './compte-form.service';

describe('Compte Form Service', () => {
  let service: CompteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompteFormService);
  });

  describe('Service methods', () => {
    describe('createCompteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCompteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numcompte: expect.any(Object),
            agence: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing ICompte should create a new form with FormGroup', () => {
        const formGroup = service.createCompteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numcompte: expect.any(Object),
            agence: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getCompte', () => {
      it('should return NewCompte for default Compte initial value', () => {
        const formGroup = service.createCompteFormGroup(sampleWithNewData);

        const compte = service.getCompte(formGroup) as any;

        expect(compte).toMatchObject(sampleWithNewData);
      });

      it('should return NewCompte for empty Compte initial value', () => {
        const formGroup = service.createCompteFormGroup();

        const compte = service.getCompte(formGroup) as any;

        expect(compte).toMatchObject({});
      });

      it('should return ICompte', () => {
        const formGroup = service.createCompteFormGroup(sampleWithRequiredData);

        const compte = service.getCompte(formGroup) as any;

        expect(compte).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICompte should not enable id FormControl', () => {
        const formGroup = service.createCompteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCompte should disable id FormControl', () => {
        const formGroup = service.createCompteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
