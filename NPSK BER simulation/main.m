symbolNumber = 5;           % number of distinct symbols
amplitude = 10;             % amplitude of each symbol
sigma = 1:1/2:5;            % The vector that contains the standard 
                            % deviation of AWGN for the values that are of
                            % interest.
sampleNumber = 10^6;        % number of random samplings

tic
sizeOfSigma = size(sigma,2);
BER = zeros(sizeOfSigma);
for i=1:sizeOfSigma
   BER(i) = NPSKsim(symbolNumber,amplitude,sigma(i),sampleNumber);
end
toc
figure;
plot(sigma,BER)