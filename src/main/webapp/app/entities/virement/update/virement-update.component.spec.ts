import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICompte } from 'app/entities/compte/compte.model';
import { CompteService } from 'app/entities/compte/service/compte.service';
import { VirementService } from '../service/virement.service';
import { IVirement } from '../virement.model';
import { VirementFormService } from './virement-form.service';

import { VirementUpdateComponent } from './virement-update.component';

describe('Virement Management Update Component', () => {
  let comp: VirementUpdateComponent;
  let fixture: ComponentFixture<VirementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let virementFormService: VirementFormService;
  let virementService: VirementService;
  let compteService: CompteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VirementUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VirementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VirementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    virementFormService = TestBed.inject(VirementFormService);
    virementService = TestBed.inject(VirementService);
    compteService = TestBed.inject(CompteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Compte query and add missing value', () => {
      const virement: IVirement = { id: 14177 };
      const compte: ICompte = { id: 21096 };
      virement.compte = compte;
      const beneficiaire: ICompte = { id: 21096 };
      virement.beneficiaire = beneficiaire;

      const compteCollection: ICompte[] = [{ id: 21096 }];
      jest.spyOn(compteService, 'query').mockReturnValue(of(new HttpResponse({ body: compteCollection })));
      const additionalComptes = [compte, beneficiaire];
      const expectedCollection: ICompte[] = [...additionalComptes, ...compteCollection];
      jest.spyOn(compteService, 'addCompteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ virement });
      comp.ngOnInit();

      expect(compteService.query).toHaveBeenCalled();
      expect(compteService.addCompteToCollectionIfMissing).toHaveBeenCalledWith(
        compteCollection,
        ...additionalComptes.map(expect.objectContaining),
      );
      expect(comp.comptesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const virement: IVirement = { id: 14177 };
      const compte: ICompte = { id: 21096 };
      virement.compte = compte;
      const beneficiaire: ICompte = { id: 21096 };
      virement.beneficiaire = beneficiaire;

      activatedRoute.data = of({ virement });
      comp.ngOnInit();

      expect(comp.comptesSharedCollection).toContainEqual(compte);
      expect(comp.comptesSharedCollection).toContainEqual(beneficiaire);
      expect(comp.virement).toEqual(virement);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVirement>>();
      const virement = { id: 13388 };
      jest.spyOn(virementFormService, 'getVirement').mockReturnValue(virement);
      jest.spyOn(virementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ virement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: virement }));
      saveSubject.complete();

      // THEN
      expect(virementFormService.getVirement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(virementService.update).toHaveBeenCalledWith(expect.objectContaining(virement));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVirement>>();
      const virement = { id: 13388 };
      jest.spyOn(virementFormService, 'getVirement').mockReturnValue({ id: null });
      jest.spyOn(virementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ virement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: virement }));
      saveSubject.complete();

      // THEN
      expect(virementFormService.getVirement).toHaveBeenCalled();
      expect(virementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVirement>>();
      const virement = { id: 13388 };
      jest.spyOn(virementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ virement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(virementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCompte', () => {
      it('should forward to compteService', () => {
        const entity = { id: 21096 };
        const entity2 = { id: 28274 };
        jest.spyOn(compteService, 'compareCompte');
        comp.compareCompte(entity, entity2);
        expect(compteService.compareCompte).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
