import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LicentaTestModule } from '../../../test.module';
import { LicentaDetailComponent } from 'app/entities/licenta/licenta-detail.component';
import { Licenta } from 'app/shared/model/licenta.model';

describe('Component Tests', () => {
  describe('Licenta Management Detail Component', () => {
    let comp: LicentaDetailComponent;
    let fixture: ComponentFixture<LicentaDetailComponent>;
    const route = ({ data: of({ licenta: new Licenta(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LicentaTestModule],
        declarations: [LicentaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LicentaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LicentaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load licenta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.licenta).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
