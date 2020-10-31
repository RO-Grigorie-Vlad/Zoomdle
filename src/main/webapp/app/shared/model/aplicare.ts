import { AplicareDTO } from './aplicare-dto';

describe('AplicareDTO', () => {
  it('should create an instance', () => {
    expect(new AplicareDTO('acestUser', 1)).toBeTruthy();
  });
});
