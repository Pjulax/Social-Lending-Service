package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserRepository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BorrowerServiceImpl implements BorrowerService {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;



    @Override
    public Auction createNewAuctionSinceNow(Command.CreateNewAuctionSinceNow createNewAuctionSinceNowCommand) {
        Optional<User> userOptional = userRepository.findById(createNewAuctionSinceNowCommand.getUserId());
        if(userOptional.isEmpty())
            return null; // throw borrower not found
        User borrower = userOptional.get();
        Auction auction = Auction.builder()
                .loanAmount(createNewAuctionSinceNowCommand.getLoanAmount())
                .beginDate(Calendar.getInstance().getTime())
                .endDate( createNewAuctionSinceNowCommand.getEndDate())
                .numberOfInstallments(createNewAuctionSinceNowCommand.getNumberOfInstallments())
                .build();
        List<Auction> auctionList = borrower.getAuctions();
        auction = auctionRepository.save(auction);
        auctionList.add(auction);
        User borrower2 = User.builder().id(borrower.getId()).auctions(auctionList).build();
        userRepository.save(borrower2);
        return auction;
    }

    @Override
    public List<Auction> getAllAuctions(Query.GetBorrowersAllAuctions getBorrowersAllAuctionsQuery) {
        Optional<User> userOptional = userRepository.findById(getBorrowersAllAuctionsQuery.getUserId());
        if(userOptional.isEmpty())
            return null; // throw borrower not found
        return userOptional.get().getAuctions();
    }
}
