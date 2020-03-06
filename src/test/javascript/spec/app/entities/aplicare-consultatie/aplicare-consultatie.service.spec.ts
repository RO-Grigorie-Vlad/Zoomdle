import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AplicareConsultatieService } from 'app/entities/aplicare-consultatie/aplicare-consultatie.service';
import { IAplicareConsultatie, AplicareConsultatie } from 'app/shared/model/aplicare-consultatie.model';

describe('Service Tests', () => {
  describe('AplicareConsultatie Service', () => {
    let injector: TestBed;
    let service: AplicareConsultatieService;
    let httpMock: HttpTestingController;
    let elemDefault: IAplicareConsultatie;
    let expectedResult: IAplicareConsultatie | IAplicareConsultatie[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(AplicareConsultatieService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new AplicareConsultatie(0, false, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a AplicareConsultatie', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AplicareConsultatie()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AplicareConsultatie', () => {
        const returnedFromService = Object.assign(
          {
            rezolvata: true,
            acceptata: true
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AplicareConsultatie', () => {
        const returnedFromService = Object.assign(
          {
            rezolvata: true,
            acceptata: true
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a AplicareConsultatie', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
