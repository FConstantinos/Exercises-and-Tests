% This functions computes the Bit Error Rate(BER) of an N-PSK (Phase Shift
% Keying) scheme. An N-PSK scheme is uniquely defined by the number of
% phase shifted symbols and the amplitude of the symbols. The number of
% errors is affected by the standard deviation of the channel's Additive
% White Gaussian Noise (AWGN) denoted sigma. The number of symbols that are
% transmitted is also provided. Due to the law of large numbers, the larger
% the number of transmissions, the closer the BER will be to the real one.

% input parameters:

% symbolNumber      number of distinct symbols
% amplitude         amplitude of each symbol
% sigma             standard deviation of AWGN
% sampleNumber      number of random samplings (number of symbols to be
%                   transmitted)

function BER = NPSKsim(symbolNumber,amplitude,sigma,sampleNumber)

% derived parameters
angles = (2*pi/symbolNumber)*(0:symbolNumber-1);        % the angle of each symbol 
                                                        % on the complex plane
                                                        
symbolPoints = amplitude*(cos(angles)+1i*sin(angles));  % the point of each symbol
                                                        % on the complex plane
% Generate the transmission symbols.
transmissionSymbols =  symbolPoints(unidrnd(symbolNumber,1,sampleNumber));

% Embed AWGN on the transmissions.
AWGNoise = normrnd(0,sigma,1,sampleNumber)+1i*normrnd(0,sigma,1,sampleNumber);
outputSymbols = transmissionSymbols + AWGNoise;

% Find the angles of each output (received Signals).
outputAngles = mod( wrapTo2Pi(2*pi+angle(outputSymbols)),2*pi ); % there is also a 
                                                                 % conversion of angle to 
                                                                 % [0,2pi)

% Let outputAngles = alpha * 2*pi/symbolNumber and compute alpha.
alpha = (symbolNumber/(2*pi))*outputAngles;

% Find the nearest symbol for each outputSymbol. 
% This is done by:
% 1) finding the two nearest symbols. Let the two nearest symbols be 
% represented by the angles n*2*pi/symbolNumber and 
% (n+1)*2*pi/symbolNumber. We know that:

% n*2*pi/symbolNumber <= outputAnlges <= (n+1)*2*pi/symbolNumber

% for some n. Due to the definition of alpha, we get:

% n*2*pi/symbolNumber <= alpha*2*pi/symbolNumber <= (n+1)*2*pi/symbolNumber

% from which we get:

% n <= alpha <= (n+1)

% Thus we can compute n by taking the floor of alpha (squeezed between 2
% consequtive integers). Afterwards, we just compute where alpha is
% nearer (n or n+1 ?). Since 2*pi/symbolNumber is a common component of all
% the above angles, the result gives us the angle of the symbol where
% outputAngles is closer. This is the symbol we are looking for.

n = floor(alpha);
[~,I] = min([abs(n-alpha);abs(n+1-alpha)],[],1);    

% I is either 1 or 2. 
% If I = 1 then the nearest symbol has an angle of n*2*pi/symbolNumber. 
% Otherwise, the nearest symbol has an angle of (n+1)*2*pi/symbolNumber.
                                                    
nearest = mod(n+I-1,symbolNumber)+1; % mod is required to return to the first symbol if 
                                     % n = symbolNumber-1 and I = 2.

% Compute empirical BER.
errors = nnz(transmissionSymbols-symbolPoints(nearest));
BER = errors/sampleNumber;

% Plot the points.
% figure;
% plot (outputSymbols,'o');
