import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IVirement } from '../virement.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../virement.test-samples';

import { VirementService } from './virement.service';

const requireRestSample: IVirement = {
  ...sampleWithRequiredData,
};

describe('Virement Service', () => {
  let service: VirementService;
  let httpMock: HttpTestingController;
  let expectedResult: IVirement | IVirement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VirementService);
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

    it('should create a Virement', () => {
      const virement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(virement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Virement', () => {
      const virement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(virement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Virement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Virement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Virement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVirementToCollectionIfMissing', () => {
      it('should add a Virement to an empty array', () => {
        const virement: IVirement = sampleWithRequiredData;
        expectedResult = service.addVirementToCollectionIfMissing([], virement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(virement);
      });

      it('should not add a Virement to an array that contains it', () => {
        const virement: IVirement = sampleWithRequiredData;
        const virementCollection: IVirement[] = [
          {
            ...virement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVirementToCollectionIfMissing(virementCollection, virement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Virement to an array that doesn't contain it", () => {
        const virement: IVirement = sampleWithRequiredData;
        const virementCollection: IVirement[] = [sampleWithPartialData];
        expectedResult = service.addVirementToCollectionIfMissing(virementCollection, virement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(virement);
      });

      it('should add only unique Virement to an array', () => {
        const virementArray: IVirement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const virementCollection: IVirement[] = [sampleWithRequiredData];
        expectedResult = service.addVirementToCollectionIfMissing(virementCollection, ...virementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const virement: IVirement = sampleWithRequiredData;
        const virement2: IVirement = sampleWithPartialData;
        expectedResult = service.addVirementToCollectionIfMissing([], virement, virement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(virement);
        expect(expectedResult).toContain(virement2);
      });

      it('should accept null and undefined values', () => {
        const virement: IVirement = sampleWithRequiredData;
        expectedResult = service.addVirementToCollectionIfMissing([], null, virement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(virement);
      });

      it('should return initial array if no Virement is added', () => {
        const virementCollection: IVirement[] = [sampleWithRequiredData];
        expectedResult = service.addVirementToCollectionIfMissing(virementCollection, undefined, null);
        expect(expectedResult).toEqual(virementCollection);
      });
    });

    describe('compareVirement', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVirement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13388 };
        const entity2 = null;

        const compareResult1 = service.compareVirement(entity1, entity2);
        const compareResult2 = service.compareVirement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13388 };
        const entity2 = { id: 14177 };

        const compareResult1 = service.compareVirement(entity1, entity2);
        const compareResult2 = service.compareVirement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 13388 };
        const entity2 = { id: 13388 };

        const compareResult1 = service.compareVirement(entity1, entity2);
        const compareResult2 = service.compareVirement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
