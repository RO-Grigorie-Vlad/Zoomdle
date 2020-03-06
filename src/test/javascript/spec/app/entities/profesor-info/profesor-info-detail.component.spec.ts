import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LicentaTestModule } from '../../../test.module';
import { ProfesorInfoDetailComponent } from 'app/entities/profesor-info/profesor-info-detail.component';
import { ProfesorInfo } from 'app/shared/model/profesor-info.model';

describe('Component Tests', () => {
  describe('ProfesorInfo Management Detail Component', () => {
    let comp: ProfesorInfoDetailComponent;
    let fixture: ComponentFixture<ProfesorInfoDetailComponent>;
    const route = ({ data: of({ profesorInfo: new ProfesorInfo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LicentaTestModule],
        declarations: [ProfesorInfoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ProfesorInfoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProfesorInfoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load profesorInfo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.profesorInfo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
