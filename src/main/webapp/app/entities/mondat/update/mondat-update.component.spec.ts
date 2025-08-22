import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICompte } from 'app/entities/compte/compte.model';
import { CompteService } from 'app/entities/compte/service/compte.service';
import { MondatService } from '../service/mondat.service';
import { IMondat } from '../mondat.model';
import { MondatFormService } from './mondat-form.service';

import { MondatUpdateComponent } from './mondat-update.component';

describe('Mondat Management Update Component', () => {
  let comp: MondatUpdateComponent;
  let fixture: ComponentFixture<MondatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mondatFormService: MondatFormService;
  let mondatService: MondatService;
  let compteService: CompteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MondatUpdateComponent],
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
      .overrideTemplate(MondatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MondatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mondatFormService = TestBed.inject(MondatFormService);
    mondatService = TestBed.inject(MondatService);
    compteService = TestBed.inject(CompteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Compte query and add missing value', () => {
      const mondat: IMondat = { id: 21971 };
      const compte: ICompte = { id: 21096 };
      mondat.compte = compte;

      const compteCollection: ICompte[] = [{ id: 21096 }];
      jest.spyOn(compteService, 'query').mockReturnValue(of(new HttpResponse({ body: compteCollection })));
      const additionalComptes = [compte];
      const expectedCollection: ICompte[] = [...additionalComptes, ...compteCollection];
      jest.spyOn(compteService, 'addCompteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mondat });
      comp.ngOnInit();

      expect(compteService.query).toHaveBeenCalled();
      expect(compteService.addCompteToCollectionIfMissing).toHaveBeenCalledWith(
        compteCollection,
        ...additionalComptes.map(expect.objectContaining),
      );
      expect(comp.comptesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const mondat: IMondat = { id: 21971 };
      const compte: ICompte = { id: 21096 };
      mondat.compte = compte;

      activatedRoute.data = of({ mondat });
      comp.ngOnInit();

      expect(comp.comptesSharedCollection).toContainEqual(compte);
      expect(comp.mondat).toEqual(mondat);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMondat>>();
      const mondat = { id: 21136 };
      jest.spyOn(mondatFormService, 'getMondat').mockReturnValue(mondat);
      jest.spyOn(mondatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mondat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mondat }));
      saveSubject.complete();

      // THEN
      expect(mondatFormService.getMondat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mondatService.update).toHaveBeenCalledWith(expect.objectContaining(mondat));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMondat>>();
      const mondat = { id: 21136 };
      jest.spyOn(mondatFormService, 'getMondat').mockReturnValue({ id: null });
      jest.spyOn(mondatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mondat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mondat }));
      saveSubject.complete();

      // THEN
      expect(mondatFormService.getMondat).toHaveBeenCalled();
      expect(mondatService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMondat>>();
      const mondat = { id: 21136 };
      jest.spyOn(mondatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mondat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mondatService.update).toHaveBeenCalled();
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
