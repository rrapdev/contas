import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBeneficiario } from '../beneficiario.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../beneficiario.test-samples';

import { BeneficiarioService } from './beneficiario.service';

const requireRestSample: IBeneficiario = {
  ...sampleWithRequiredData,
};

describe('Beneficiario Service', () => {
  let service: BeneficiarioService;
  let httpMock: HttpTestingController;
  let expectedResult: IBeneficiario | IBeneficiario[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BeneficiarioService);
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

    it('should create a Beneficiario', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const beneficiario = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(beneficiario).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Beneficiario', () => {
      const beneficiario = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(beneficiario).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Beneficiario', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Beneficiario', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Beneficiario', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBeneficiarioToCollectionIfMissing', () => {
      it('should add a Beneficiario to an empty array', () => {
        const beneficiario: IBeneficiario = sampleWithRequiredData;
        expectedResult = service.addBeneficiarioToCollectionIfMissing([], beneficiario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(beneficiario);
      });

      it('should not add a Beneficiario to an array that contains it', () => {
        const beneficiario: IBeneficiario = sampleWithRequiredData;
        const beneficiarioCollection: IBeneficiario[] = [
          {
            ...beneficiario,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBeneficiarioToCollectionIfMissing(beneficiarioCollection, beneficiario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Beneficiario to an array that doesn't contain it", () => {
        const beneficiario: IBeneficiario = sampleWithRequiredData;
        const beneficiarioCollection: IBeneficiario[] = [sampleWithPartialData];
        expectedResult = service.addBeneficiarioToCollectionIfMissing(beneficiarioCollection, beneficiario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(beneficiario);
      });

      it('should add only unique Beneficiario to an array', () => {
        const beneficiarioArray: IBeneficiario[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const beneficiarioCollection: IBeneficiario[] = [sampleWithRequiredData];
        expectedResult = service.addBeneficiarioToCollectionIfMissing(beneficiarioCollection, ...beneficiarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const beneficiario: IBeneficiario = sampleWithRequiredData;
        const beneficiario2: IBeneficiario = sampleWithPartialData;
        expectedResult = service.addBeneficiarioToCollectionIfMissing([], beneficiario, beneficiario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(beneficiario);
        expect(expectedResult).toContain(beneficiario2);
      });

      it('should accept null and undefined values', () => {
        const beneficiario: IBeneficiario = sampleWithRequiredData;
        expectedResult = service.addBeneficiarioToCollectionIfMissing([], null, beneficiario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(beneficiario);
      });

      it('should return initial array if no Beneficiario is added', () => {
        const beneficiarioCollection: IBeneficiario[] = [sampleWithRequiredData];
        expectedResult = service.addBeneficiarioToCollectionIfMissing(beneficiarioCollection, undefined, null);
        expect(expectedResult).toEqual(beneficiarioCollection);
      });
    });

    describe('compareBeneficiario', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBeneficiario(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBeneficiario(entity1, entity2);
        const compareResult2 = service.compareBeneficiario(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBeneficiario(entity1, entity2);
        const compareResult2 = service.compareBeneficiario(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBeneficiario(entity1, entity2);
        const compareResult2 = service.compareBeneficiario(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
