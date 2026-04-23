describe('Admin flow - review and delete articles', () => {

  beforeEach(() => {
    cy.visit('/signin');

    cy.get('input[placeholder="Username"]').type('admin');
    cy.get('input[placeholder="Password"]').type('admin');

    cy.get('input[value="Login"]').click();

    cy.contains('Logout').should('be.visible');
  });

  it('approves and deletes an article', () => {

    cy.contains('Review').click();

    cy.contains('Subway').should('be.visible');

    cy.contains('Start Review').click();
    cy.contains('Approve').click();

    cy.contains('Admin').click();

    cy.contains('Subway').should('be.visible');

    // delete
    cy.contains('Subway')
      .parent()
      .contains('Delete')
      .click();

    cy.contains('Subway').should('not.exist');
  });

});
