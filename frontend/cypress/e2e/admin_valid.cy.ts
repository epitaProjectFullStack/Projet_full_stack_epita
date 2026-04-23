describe('Admin flow - review and delete articles', () => {

  beforeEach(() => {
    cy.visit('/signin');

    cy.get('input[placeholder="Username"]').type('admin');
    cy.get('input[placeholder="Password"]').type('admin');

    cy.get('input[value="Login"]').click();

    cy.contains('Logout').should('be.visible');
  });
  it('admin approves article and it appears on home', () => {

    cy.contains('Review').click();
    cy.contains('Start Review').click();
    cy.contains('Approve').click();
    cy.contains('Home').click();
    cy.contains('Subway').should('be.visible');
  });
});
