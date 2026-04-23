describe('Page Register', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('affiche le formulaire inscription', () => {
    cy.get('input[placeholder="Username"]').should('be.visible');
    cy.get('input[placeholder="Email"]').should('be.visible');
    cy.get('input[placeholder="Password"]').should('be.visible');
    cy.get('input[value="Register"]').should('be.visible');
  });

  it('inscription réussie', () => {
    const unique = Date.now();
    cy.get('input[placeholder="Username"]').type(`user${unique}`);
    cy.get('input[placeholder="Email"]').type(`user${unique}@test.com`);
    cy.get('input[placeholder="Password"]').type('Password123!');
    cy.get('input[value="Register"]').click();
    cy.get('.alert-success').should('contain', 'Register Succeful');
  });
/*not submit registration*/
  it('inscription sans remplir les champs', () => {
    cy.intercept('POST', '/register').as('register');
    cy.get('input[value="Register"]').click();
    cy.get('@register.all').should('have.length', 0);
    cy.url().should('include', '/register');
  });
/*no request send*/
  it('should not send request', () => {
    cy.intercept('POST', '/register').as('register');
    cy.get('input[value="Register"]').click();
    cy.wait(300);
    cy.get('@register.all').then((calls) => {
	   expect(calls.length).to.eq(0);
    });
  });
/*no navigation*/
  it('url stays the same', () => {
    cy.get('input[value="Register"]').click();
    cy.location('pathname').should('eq', '/register');
  });

});
