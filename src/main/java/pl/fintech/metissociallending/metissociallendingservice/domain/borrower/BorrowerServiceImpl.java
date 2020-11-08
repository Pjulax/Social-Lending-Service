package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final AuctionRepository auctionRepository;

    @Override
    public Borrower createBorrower(Command.CreateBorrower createBorrowerCommand) {
        Borrower borrower = new Borrower();
        return borrowerRepository.save(borrower);
    }

    @Override
    public Auction createNewAuctionSinceNow(Command.CreateNewAuctionSinceNow createNewAuctionSinceNowCommand) {
        Optional<Borrower> borrowerOptional = borrowerRepository.findById(createNewAuctionSinceNowCommand.getBorrowerId());
        if(borrowerOptional.isEmpty())
            return null; // throw borrower not found
        Borrower borrower = borrowerOptional.get();
        Auction auction = Auction.builder()
                .loanAmount(createNewAuctionSinceNowCommand.getLoanAmount())
                .beginDate(Calendar.getInstance().getTime())
                .endDate( createNewAuctionSinceNowCommand.getEndDate())
                .beginLoanDate(createNewAuctionSinceNowCommand.getBeginLoanDate())
                .endLoanDate(createNewAuctionSinceNowCommand.getEndLoanDate())
                .installmentsFrequencyInYear(createNewAuctionSinceNowCommand.getInstallmentsFrequencyInYear())
                .build();
        List<Auction> auctionList = borrower.getAuctions();
        auction = auctionRepository.save(auction);
        auctionList.add(auction);
        Borrower borrower2 = Borrower.builder().id(borrower.getId()).auctions(auctionList).build();
        borrowerRepository.save(borrower2);
        return auction;
    }

    @Override
    public List<Auction> getAllAuctions(Query.GetBorrowersAllAuctions getBorrowersAllAuctionsQuery) {
        Optional<Borrower> borrowerOptional = borrowerRepository.findById(getBorrowersAllAuctionsQuery.getBorrowerId());
        if(borrowerOptional.isEmpty())
            return null; // throw borrower not found
        return borrowerOptional.get().getAuctions();
    }
}
