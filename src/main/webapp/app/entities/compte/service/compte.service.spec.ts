import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICompte } from '../compte.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../compte.test-samples';

import { CompteService } from './compte.service';

const requireRestSample: ICompte = {
  ...sampleWithRequiredData,
};

describe('Compte Service', () => {
  let service: CompteService;
  let httpMock: HttpTestingController;
  let expectedResult: ICompte | ICompte[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CompteService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Compte', () => {
      const compte = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(compte).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Compte', () => {
      const compte = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(compte).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Compte', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Compte', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Compte', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCompteToCollectionIfMissing', () => {
      it('should add a Compte to an empty array', () => {
        const compte: ICompte = sampleWithRequiredData;
        expectedResult = service.addCompteToCollectionIfMissing([], compte);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compte);
      });

      it('should not add a Compte to an array that contains it', () => {
        const compte: ICompte = sampleWithRequiredData;
        const compteCollection: ICompte[] = [
          {
            ...compte,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCompteToCollectionIfMissing(compteCollection, compte);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Compte to an array that doesn't contain it", () => {
        const compte: ICompte = sampleWithRequiredData;
        const compteCollection: ICompte[] = [sampleWithPartialData];
        expectedResult = service.addCompteToCollectionIfMissing(compteCollection, compte);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compte);
      });

      it('should add only unique Compte to an array', () => {
        const compteArray: ICompte[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const compteCollection: ICompte[] = [sampleWithRequiredData];
        expectedResult = service.addCompteToCollectionIfMissing(compteCollection, ...compteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const compte: ICompte = sampleWithRequiredData;
        const compte2: ICompte = sampleWithPartialData;
        expectedResult = service.addCompteToCollectionIfMissing([], compte, compte2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compte);
        expect(expectedResult).toContain(compte2);
      });

      it('should accept null and undefined values', () => {
        const compte: ICompte = sampleWithRequiredData;
        expectedResult = service.addCompteToCollectionIfMissing([], null, compte, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compte);
      });

      it('should return initial array if no Compte is added', () => {
        const compteCollection: ICompte[] = [sampleWithRequiredData];
        expectedResult = service.addCompteToCollectionIfMissing(compteCollection, undefined, null);
        expect(expectedResult).toEqual(compteCollection);
      });
    });

    describe('compareCompte', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCompte(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 21096 };
        const entity2 = null;

        const compareResult1 = service.compareCompte(entity1, entity2);
        const compareResult2 = service.compareCompte(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 21096 };
        const entity2 = { id: 28274 };

        const compareResult1 = service.compareCompte(entity1, entity2);
        const compareResult2 = service.compareCompte(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 21096 };
        const entity2 = { id: 21096 };

        const compareResult1 = service.compareCompte(entity1, entity2);
        const compareResult2 = service.compareCompte(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
